package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CommandRuler;

/**
 * команда выводящая все доступные команды
 */
public class Help extends Command {
    private final CommandRuler commandRuler;

    public Help( CommandRuler commandRuler) {
        super("help", "вывести справку по доступным командам");
        this.commandRuler = commandRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password) {
        if (!arguments[1].isEmpty()) {
            //console.println("Неправильное количество аргументов!");
            //console.println("Использование: '" + getName() + "'");
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
        }

/*        commandRuler.getCommands().values().forEach(command -> {
            console.printTable(command.getName(), command.getDescription());
        });*/

        StringBuilder result = new StringBuilder();
        commandRuler.getCommands().values().forEach(command -> {
            result.append(command.getName() + " : " + command.getDescription()+"\n\n");
        });
        result.append("execute_script : выполнить скрипт указанного файла");
        return new Response(result.toString());
    }
}
