package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CollectionRuler;
import com.f4d4.server.rulers.DatabaseRuler;

import java.sql.SQLException;


/**
 * добавление элемента в коллекцию
 */

public class Add extends Command{
    private final CollectionRuler collectionRuler;

    public Add( CollectionRuler collectionRuler){
        super("add", "добавить новый элемент в коллекцию");
        this.collectionRuler=collectionRuler;
    }

    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */

    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        try{
            if(!arguments[1].isEmpty()){
                return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
            }

            ticket.setLogin(login);
            ticket.setUser_id(collectionRuler.getUserid(login));
            if(ticket!= null&&ticket.validate()){
                collectionRuler.addTOcollection(ticket , login);
                return new Response("Ticket добавлен!");
            }else{
                return new Response("Поля Ticket не валидны! Ticket не создан!");
            }
        }catch (SQLException e ){
            return new Response("Ошибка при добавлении билета");
        } catch (Exception e) {
            return new Response("Непредвиденная ошибка при добавлении билета");
        }
    }
}
