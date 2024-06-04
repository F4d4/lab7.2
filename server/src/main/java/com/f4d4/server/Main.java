package com.f4d4.server;

//import com.f4d4.client.commands.Execute_script;
import com.f4d4.general.tools.Console;
import com.f4d4.general.tools.MyConsole;
import com.f4d4.server.commands.*;
import com.f4d4.server.managers.SocketServer;
import com.f4d4.server.rulers.CollectionRuler;
import com.f4d4.server.rulers.CommandRuler;
import com.f4d4.server.rulers.DatabaseRuler;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Console console = new MyConsole();

        DatabaseRuler databaseRuler = new DatabaseRuler();

        databaseRuler.connect();

        CollectionRuler collectionRuler= new CollectionRuler(databaseRuler);
        if (!collectionRuler.init()) {
            System.exit(1);
        }
        CommandRuler commandRuler = new CommandRuler(){{
            register("info", new Info(collectionRuler));
            register("add" , new Add(collectionRuler));
            register("add_if_min", new AddIfMin(collectionRuler));
            register("update_by_id",new UpdateById(collectionRuler , databaseRuler) );
            register("show" , new Show(collectionRuler));
            register("filter_greater_than_price", new FilterGreaterThanPrice(collectionRuler));
            register("filter_starts_with_name",new FilterStartsWIthName(collectionRuler));
            register("help", new Help(this));
            register("clear", new Clear(collectionRuler));
            register("print_field_ascending_event", new PrintFieldAscendingEvent(collectionRuler));
            register("save" , new Save(collectionRuler));
            register("exit" , new Exit());
            register("history" , new History(this));
            register("remove_by_id" , new RemoveById(collectionRuler , databaseRuler));
            register("remove_first" , new RemoveFirst(collectionRuler , databaseRuler));
            register("registration", new Registration(databaseRuler));
            register("login" , new Login(databaseRuler));
        }};

        new SocketServer("localhost" , 64379 , commandRuler).start();
    }
}
