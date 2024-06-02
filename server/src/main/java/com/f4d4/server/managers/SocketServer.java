package com.f4d4.server.managers;

import com.f4d4.general.facility.Request;
import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.f4d4.server.rulers.CommandRuler;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer {
    public static final Logger log = LoggerFactory.getLogger(SocketServer.class);
    private final CommandRuler commandRuler;
    private Selector selector;
    private InetSocketAddress address;
    private Set<SocketChannel> session;
    private final ExecutorService readThreadPool;
    private final ExecutorService sendThreadPool;

    public SocketServer(String host, int port, CommandRuler commandRuler) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        this.commandRuler = commandRuler;
        this.readThreadPool = Executors.newFixedThreadPool(10); // количество потоков можно настроить
        this.sendThreadPool = Executors.newCachedThreadPool();
    }

    public void start() throws IOException, ClassNotFoundException {
        this.selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

        log.info("Server started...");
        new Thread(() -> {
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    String input = consoleReader.readLine();
                    String[] tokens = (input.trim() + " ").split(" ", 2);
                    tokens[1] = tokens[1].trim();
                    String executingCommand = tokens[0];
                    var exitCommand = commandRuler.getCommands().get("exit");
                    if (executingCommand.equals("exit")) {
                        Response serverResponseExit = exitCommand.apply(tokens, null, null, null);
                    } else {
                        log.warn("Внимание! Введенная вами команда отсутствует в базе сервера. Вам доступны следующие две команды: save, exit. Введите любую из них.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while (true) {
            this.selector.select();
            Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
            while (keys.hasNext()) {
                SelectionKey key = keys.next();
                keys.remove();
                if (!key.isValid()) continue;
                if (key.isAcceptable()) accept(key);
                else if (key.isReadable()) {
                    key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
                    readThreadPool.submit(() -> {
                        try {
                            read(key);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            key.interestOps(key.interestOps() | SelectionKey.OP_READ);
                            this.selector.wakeup();
                        }
                    });
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
        this.session.add(channel);
        log.info("Подключился новый пользователь: " + channel.socket().getRemoteSocketAddress() + "\n");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        while (true) {
            try {
                int numRead = channel.read(buffer);

                if (numRead == -1) {
                    // Клиент закрыл соединение
                    this.session.remove(channel);
                    log.info("Пользователь отключился: " + channel.socket().getRemoteSocketAddress() + "\n");
                    key.cancel();
                    return;
                }

                if (numRead == 0) {
                    // Нет данных для чтения
                    break;
                }

                buffer.flip();
                byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            } catch (SocketException e) {
                this.session.remove(channel);
                log.info("Пользователь внезапно отключился: " + channel.socket().getRemoteSocketAddress() + "\n");
                key.cancel();
                return;
            }
        }

        byte[] data = byteArrayOutputStream.toByteArray();
        if (data.length > 0) {
            new Thread(() -> {
                try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(data))) {
                    Request request = (Request) oi.readObject();
                    String gotData = request.getCommandMassage();
                    Ticket gotTicket = request.getTicket();
                    String gotLogin = request.getLogin();
                    String gotPassword = request.getPassword();
                    log.info("Получено: " + gotData + " | Ticket:" + gotTicket);

                    String[] tokens = (gotData.trim() + " ").split(" ", 2);
                    tokens[1] = tokens[1].trim();
                    String executingCommand = tokens[0];
                    commandRuler.addToHistory(executingCommand);
                    var command = commandRuler.getCommands().get(executingCommand);

                    if (command == null && !executingCommand.equals("execute_script")) {
                        sendAnswer(new Response("Команда '" + tokens[0] + "' не найдена. Наберите 'help' для справки\n"), channel);
                        return;
                    }

                    Response response = command.apply(tokens, gotTicket, gotLogin, gotPassword);
                    sendThreadPool.submit(()->{
                        sendAnswer(response,channel);
                    });
                } catch (ClassNotFoundException | IOException | SQLException e) {
                    log.error("Ошибка обработки запроса: " + e.getMessage());
                }
            }).start();
        }
    }

    public void sendAnswer(Response response, SocketChannel client) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(response);
            objectOutputStream.close();
            ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
            while (buffer.hasRemaining()) {
                client.write(buffer);
            }
            log.info("Ответ клиенту отправлен");
        } catch (IOException e) {
            log.error("Ошибка отправки ответа: " + e.getMessage());
        }
    }
}
