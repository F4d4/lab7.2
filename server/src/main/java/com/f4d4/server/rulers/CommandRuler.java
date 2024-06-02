package com.f4d4.server.rulers;

import com.f4d4.server.commands.Command;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс Руководителя командами
 */
public class CommandRuler {
    private final Map<String , Command> commands= new LinkedHashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    /**
     * Добавляет команду в список
     */
    public void register(String commandName, Command command) {
        commands.put(commandName, command);
    }

    /**
     * Возвращает все команды
     */
    public Map<String, Command> getCommands() {
        return commands;
    }

    /**
     * Возвращает историю команд
     */
    public List<String> getCommandHistory() {
        return commandHistory;
    }
    /**
     * Добавляет использованную команду в историю команд
     */
    public void addToHistory(String command) {
        commandHistory.add(command);
    }

}
