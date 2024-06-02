package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.DatabaseRuler;

import java.sql.SQLException;

import static com.f4d4.server.tools.PasswordHashing.*;

public class Registration extends Command{
    private DatabaseRuler databaseRuler;
    public Registration(DatabaseRuler databaseRuler){
        super("registration" , "добавить пользователя");
        this.databaseRuler = databaseRuler;
    }

    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password) throws SQLException {
        try {
            String salt = generateSalt();
            databaseRuler.insertUser(login, hashPasswordWithSalt(password,salt),salt );
            return new Response("Вы успешно зарегестрировались" , true);
        }catch(SQLException e){
            return new Response("Ошибка! Не удалось добавть пользователя");
        }

    }
}
