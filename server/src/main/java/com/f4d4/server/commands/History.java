package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CommandRuler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * команда выводящая последние 13 использованных команд
 */
public class History extends Command{
    private final CommandRuler commandRuler;

    public History( CommandRuler commandRuler) {
        super("history", "вывести последние 13 комманд");
        this.commandRuler = commandRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if (!arguments[1].isEmpty()) {
            //console.println("Неправильное количество аргументов!");
            //console.println("Использование: '" + getName() + "'");
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
        }

        List<String> myHistory = new ArrayList<>(commandRuler.getCommandHistory()); // Создаем копию CommandHistory
        int startIndex = Math.max(0, myHistory.size() - 13); // Начальный индекс для вывода последних 13 команд
        List<String> last13Commands = myHistory.subList(startIndex, myHistory.size()); // Получаем последние 13 команд

/*        last13Commands.forEach(command -> {
            console.println(" " + command);
        });*/

        String stringHistory = last13Commands.stream()
                .collect(Collectors.joining("\n"));
        return new Response(stringHistory);
    }

}
