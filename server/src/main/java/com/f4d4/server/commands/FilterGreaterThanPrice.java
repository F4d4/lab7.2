package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.general.facility.Ticket;
import com.f4d4.server.rulers.CollectionRuler;

import java.util.List;
import java.util.stream.Collectors;

/**
 * команда выводящая элементы , значение price которых больше заданного
 */
public class FilterGreaterThanPrice extends Command{
    private final CollectionRuler collectionRuler;

    public FilterGreaterThanPrice( CollectionRuler collectionRuler){
        super("filter_greater_than_price", "вывести элементы , значение price которых больше заданного");
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
        var price = Long.parseLong(arguments[1]);
        List<Ticket> tickets = filterByPrice(price);
        if (tickets.isEmpty()) {
            console.println("Ticket с ценой больше" + price + " не обнаружено.");
            return new Response("Ticket с ценой больше" + price + " не обнаружено.");
        } else {
            //console.println("Ticket с ценой " + price + ": " + tickets.size() + " шт.\n");
            String ticketsString = tickets.stream()
                    .map(Ticket::toString)
                    .collect(Collectors.joining("\n"));
            return new Response(ticketsString);
        }
    }

    private List<Ticket> filterByPrice(Long price) {
        return collectionRuler.getCollection().stream()
                .filter(ticket -> (ticket.getPrice() >price))
                .collect(Collectors.toList());
    }
}
