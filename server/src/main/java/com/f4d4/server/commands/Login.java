package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.DatabaseRuler;

import java.sql.SQLException;

public class Login extends Command {
    private DatabaseRuler databaseRuler;

    public Login(DatabaseRuler databaseRuler){
        super("login" , "авторизоваться");
        this.databaseRuler = databaseRuler;
    }

    @Override
    public Response apply(String[] arguments , Ticket ticket , String login , String password){
        try{
            if(databaseRuler.checkUser(login , password)){
                return new Response("Вы успешно вошли в систему" , true);
            }else {
                return new Response("Непредвиденная ошибка во время авторизации пользователя");
            }
        }catch (SQLException e){
            return new Response("Не удалось авторизовать пользователя.Возможно вы указали неверный логин или пароль");
        }
    }
}
