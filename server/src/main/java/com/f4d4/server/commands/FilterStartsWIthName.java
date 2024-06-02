package com.f4d4.server.commands;


import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CollectionRuler;

import java.util.List;
import java.util.stream.Collectors;

public class FilterStartsWIthName extends Command {
    private final CollectionRuler collectionRuler;
    /**
     * команда выводящая элементы ,  значение name которых начинается с заданной подстроки
     */
    public FilterStartsWIthName( CollectionRuler collectionRuler){
        super("filter_starts_with_name", "вывести элементы , значение name которых начинается с заданной подстроки ");
        this.collectionRuler=collectionRuler;

    }
    /**
     * метод выполняет команду
     *
     * @return возвращает сообщение о  успешности выполнения команды
     */
    @Override
    public Response apply(String[] arguments , Ticket ticket,String login,String password){
        if(arguments[1].isEmpty()){
            //console.println("Неправильное количество аргументов!");
            //console.println("Использование: '" + getName() + "'");
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'" );
        }
        List<Ticket> tickets= filterByName(arguments[1]);
        if(tickets.isEmpty()){
            //console.println("Ticket, чьи name содержат '" + arguments[1] + "' не обнаружено.");
            return new Response("Ticket, чьи name содержат '" + arguments[1] + "' не обнаружено.");
        }else{
            //console.println("Ticket, чьи name содержат '" + arguments[1] + "' обнаружено " + tickets.size() + " шт.\n");
            //tickets.forEach(console::println);
            String ticketsString = tickets.stream()
                    .map(Ticket::toString)
                    .collect(Collectors.joining("\n"));
            return new Response(ticketsString);
        }
    }

    private List<Ticket> filterByName(String partName) {
        return collectionRuler.getCollection().stream()
                .filter(product -> (product.getName() != null && product.getName().contains(partName)))
                .collect(Collectors.toList());
    }
}
