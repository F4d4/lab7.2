package com.f4d4.server.commands;

import com.f4d4.general.exeptions.NotFoundException;
import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CollectionRuler;
import com.f4d4.server.rulers.DatabaseRuler;

import java.sql.SQLException;

/**
 * команда обновляющая значение элемента коллекции, id которого равен заданному
 */
public class UpdateById extends Command{

    private final CollectionRuler collectionRuler;
    private final DatabaseRuler databaseRuler;

    public UpdateById( CollectionRuler collectionRuler , DatabaseRuler databaseRuler){
        super("update_by_id" , "обновить значение элемента коллекции, id которого равен заданному");

        this.collectionRuler=collectionRuler;
        this.databaseRuler = databaseRuler;
    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password) {
        if(arguments[1].isEmpty()){
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
        }
        try{
            long deletableId= Long.parseLong(arguments[1]);
            var deletable= collectionRuler.byId(deletableId);
            if (deletable == null) throw new NotFoundException();
            var userID = collectionRuler.getUserid(login);
            var checkingUserID = collectionRuler.isCorrectID(deletableId);
            if(userID == checkingUserID&&ticket!= null&&ticket.validate()){
                collectionRuler.updateTicketDB(ticket,deletableId);
                collectionRuler.updateCollection(deletable,ticket,deletableId , login);
                return new Response("Ticket обновлен");
            }else {
                return new Response("Ticket не обновлен! Возможные причины: вы попытались обновить чужой ticket " +
                        "или поля обновленного ticekt невалидны");
            }
        }catch(NotFoundException e){
            return new Response("Продукта с таким ID в коллекции нет!");
        }catch(SQLException e){
            return new Response("Ошибка обновления tikcet в базе данных");
        }
    }
}