package com.f4d4.server.commands;

import com.f4d4.general.tools.Console;
import com.f4d4.general.tools.MyConsole;


/**
 * Абстрактная команда , с помощью которой мы задаем имя и описание команды
 */
public abstract class Command implements Executable {
    private final String name;
    private final String description;

    public Console console = new MyConsole();

    public Command(String name, String description){
        this.name=name;
        this.description=description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj) return true;
        if(obj ==null|| this.getClass()!=obj.getClass()) return false;
        Command command = (Command) obj;
        return name.equals(command.name)&&description.equals(command.description);
    }


    @Override
    public int hashCode(){
        return name.hashCode()+description.hashCode();
    }
}
