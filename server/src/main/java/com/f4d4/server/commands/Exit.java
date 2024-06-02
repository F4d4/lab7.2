package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;

/**
 * команда выхода
 */
public class Exit extends Command  {


    public Exit(){
        super("exit","завершить программу");
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if(!arguments[1].isEmpty()){
            console.println("Неправильное количество аргументов!");
            console.println("Использование: '" + getName() + "'");
            return new Response("");
        }

        console.println("завершение программы");
        System.exit(1);
        return new Response("");
    }

}