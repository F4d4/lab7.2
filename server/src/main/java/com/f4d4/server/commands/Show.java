package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CollectionRuler;

/**
 * команда выводящая в стандартный поток вывода все элементы коллекции в строковом представлении
 */
public class Show extends Command  {

    private final CollectionRuler collectionRuler;
    public Show( CollectionRuler collectionRuler){
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionRuler=collectionRuler;

    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if(!arguments[1].isEmpty()){
            //console.println("Неправильное количество аргументов!");
            //console.println("Использование: '" + getName() + "'");
            return new Response("Неправильное количество аргументов!\nИспользование: '\" + getName() + \"'");
        }

        //console.println(collectionRuler);
        return new Response(collectionRuler.toString());
    }
}