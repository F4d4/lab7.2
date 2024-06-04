package com.f4d4.client.managers;

//import com.f4d4.client.commands.Execute_script;
import com.f4d4.client.commands.Execute_script;
import com.f4d4.client.tools.Ask;
import com.f4d4.general.facility.Request;
import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.general.tools.MyConsole;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SocketClient {
    MyConsole console = new MyConsole();
    private String host;
    private int port;
    public static boolean isLoggedIn = false;
    private String clientsLogin;
    private String clientsPassword;
    public SocketClient(String host, int port){
        this.port=port;
        this.host=host;
    }

    public void start() throws Exception {
        boolean connected = false;
        SocketChannel socketChannel = null;

        // Попытка подключения с задержкой
        while (!connected) {
            try {
                socketChannel = SocketChannel.open();
                socketChannel.connect(new InetSocketAddress(host, port));
                socketChannel.configureBlocking(false);
                connected = true; // Успешное подключение
            } catch (IOException e) {
                console.println("Не удалось подключиться к серверу. Попробуйте снова через 5 секунд...");
                try {
                    Thread.sleep(5000); // Задержка перед следующей попыткой
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        console.println("Подключение к серверу успешно установлено.");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                while (!isLoggedIn){
                    String tryTOlog = scanner.nextLine().trim();
                    while(true){
                        try {
                            String[] tokensLog = (tryTOlog.trim()).split(" " , 3);
                            String commandToRegistrate = tokensLog[0].trim();
                            String login = tokensLog[1].trim();
                            clientsLogin = login;
                            String password = tokensLog[2].trim();
                            clientsPassword=password;
                            if(!commandToRegistrate.equals("registration")&&!commandToRegistrate.equals("login")){
                                console.println("Неавторизованный пользователь не может использовать команды." +
                                        "\nИспользуйте registration [ваш логин] [ваш пароль] или login [ваш логин] [ваш пароль]");
                                break;
                            }else{
                                Request request = new Request(commandToRegistrate,null,login,password);
                                sendRequest(request,socketChannel);
                                break;
                            }
                        }catch (ArrayIndexOutOfBoundsException e){
                            console.println("Вы еще не авторизовались в системе. Используйте команду registration , если вы впервые на сайте." +
                                    " Если вы уже зарегистрированы , то используйте команду login\n Форматы ввода команд:\n" +
                                    "registration [ваш логин] [ваш пароль]\nlogin [ваш логин] [ваш пароль]");
                            break;
                        }
                    }
                }

                String command = scanner.nextLine().trim();
                String[] tokens = (command.trim() + " ").split(" ", 2);
                String command1 = tokens[0];

                if (command1.equals("exit")) {
                    console.println("Завершение сеанса");
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        console.println("Ошибка при закрытии соединения: " + e.getMessage());
                    }
                    System.exit(1);
                }

                if (command1.equals("add") || command1.equals("update_by_id") || command1.equals("add_if_min")) {
                    Ticket ticket = Ask.askTicket(console);
                    Request request = new Request(command, ticket , clientsLogin , clientsPassword);
                    sendRequest(request, socketChannel);
                } else if (command1.equals("execute_script")) {
                    try{
                        Execute_script.execute(command, socketChannel ,clientsLogin , clientsPassword);
                    }catch (Exception e){
                        console.println("Непредвиденная ошибка. Возможно вы ввели неправильное название файла");
                    }
                } else if(!command1.equals("save")){
                    Request request = new Request(command, null,clientsLogin , clientsPassword);
                    sendRequest(request, socketChannel);
                }
            }catch (IOException e){
                console.println("Проблемы с подключением к серверу. Попытка переподключиться ...");
                while(true){
                    try{
                        if(socketChannel!=null && socketChannel.isOpen()){
                            socketChannel.close();
                        }
                        socketChannel = SocketChannel.open();
                        socketChannel.connect(new InetSocketAddress(host, port));
                        socketChannel.configureBlocking(false);
                        console.println("Подключение восстановлено !");
                        break;
                    }catch (IOException a){
                        Thread.sleep(1000);
                    }
                }
            }
        }
    }

    public static void sendRequest(Request request , SocketChannel channel) throws IOException, ClassNotFoundException, InterruptedException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(request);
        objectOutputStream.close();
        ByteBuffer buffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

        while(buffer.hasRemaining()){
            channel.write(buffer);
        }
        System.out.println(getAnswer(channel));
    }

    public static Object getAnswer(SocketChannel channel) throws IOException, ClassNotFoundException, InterruptedException {
        Selector selector = Selector.open();
        channel.register(selector, channel.validOps());
        ByteBuffer buffer = ByteBuffer.allocate(1024); // Меньший размер буфера для частых проверок
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 10000) {
            int readyChannels = selector.select(1000); // Таймаут для select, чтобы не блокировать навсегда

            if (readyChannels == 0) {
                continue;
            }

            while (true) {
                int bytesRead = channel.read(buffer);
                if (bytesRead == -1) {
                    break; // Конец потока данных
                }
                if (bytesRead == 0) {
                    break; // Нет доступных данных для чтения
                }

                buffer.flip();
                byteArrayOutputStream.write(buffer.array(), 0, buffer.limit());
                buffer.clear();
            }

            byte[] responseBytes = byteArrayOutputStream.toByteArray();
            if (responseBytes.length > 0) {
                try (ObjectInputStream oi = new ObjectInputStream(new ByteArrayInputStream(responseBytes))) {
                    Response answer = (Response) oi.readObject();
                    boolean result = answer.getResult();
                    if(result){
                        isLoggedIn = true;
                    }
                    return answer.getMessage();
                } catch (EOFException | StreamCorruptedException e) {
                    // Не удалось десериализовать объект, возможно, не все данные получены
                    // Продолжаем чтение данных
                }
            }
        }
        return null;
    }

}
