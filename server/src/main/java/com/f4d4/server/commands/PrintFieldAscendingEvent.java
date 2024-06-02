package com.f4d4.server.commands;

import com.f4d4.general.facility.Response;
import com.f4d4.server.rulers.CollectionRuler;
import com.f4d4.general.facility.Ticket;
import java.util.List;
import java.util.stream.Collectors;

/**
 * команда выводящая значения поля event всех элементов в порядке возрастания
 */
public class PrintFieldAscendingEvent extends Command{

    private final CollectionRuler collectionRuler;

    public PrintFieldAscendingEvent( CollectionRuler collectionRuler){
        super("print_field_ascending_event","вывести значения поля event всех элементов в порядке возрастания");

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
            return new Response("Неправильное количество аргументов!\n" + "Использование: '" + getName() + "'");
        }

        var eventMinAge=filterByMinAge();
        if(eventMinAge.isEmpty()){
            //console.println("Значения event отсутствуют");
            return new Response("Значения event отсутствуют");
        }else{
            //console.println("Значения event в порядке возрастания");
            //eventMinAge.forEach(console::println);
            String minAgeString = eventMinAge.stream()
                    .map(it -> String.valueOf(it))
                    .collect(Collectors.joining("\n"));
            return new Response("\nЗначения event в порядке возрастания\n"+minAgeString);
        }

    }
    private List<Long> filterByMinAge() {
        return collectionRuler.getCollection().stream()
                .map(Ticket::getEventMinage)
                .sorted()
                .collect(Collectors.toList());
    }

}
