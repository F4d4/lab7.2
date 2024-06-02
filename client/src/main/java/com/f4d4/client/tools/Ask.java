package com.f4d4.client.tools;
import com.f4d4.general.facility.*;
import com.f4d4.general.tools.Console;

public class Ask {

    public static class AskBreak extends Exception {}
    /**
     * Собирает все введенные значения в билет
     */
    public static Ticket askTicket(Console console) throws AskBreak {
        String name = askTicketName(console);
        Coordinates coordinates = askCoordinates(console);
        Long price = askPrice(console);
        Integer discount = askDiscount(console);
        TicketType ticketType = askTicketType(console);
        Event event = askEvent(console);
        Ticket ticket = new Ticket(name, coordinates, price, discount, ticketType, event ,1 , null);
        return ticket;
    }
    /**
     * Читает значение координат
     */
    public static Coordinates askCoordinates(Console console) throws AskBreak {
        int x;
        while (true) {
            console.print("Введите координату x: ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    x = Integer.parseInt(line);
                    if(x>=-395){
                        break;
                    }else{
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    console.printError("Неправильный тип вводимых данных");
                }catch (IllegalArgumentException e){
                    console.printError("Переменная не валидна");
                }
            }
        }
        double y;
        while (true) {
            console.print("Введите координату y:");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    y = Double.parseDouble(line);
                    if(y<=99){
                        break;
                    }else{
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    console.printError("Неправильный тип вводимых данных");
                }catch (IllegalArgumentException e){
                    console.printError("Переменная не валидна");
                }
            }
        }
        return new Coordinates(x, y);

    }
    /**
     * Читает название билета
     */
    public static String askTicketName(Console console) throws AskBreak {
        String name;
        while (true) {
            console.print("Ведите название билета: ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                name = line;
                break;
            }
        }
        return name;
    }
    /**
     * Читает значение цены билета
     */
    public static Long askPrice(Console console) throws AskBreak {
        long price;
        while (true) {
            console.print("Введите цену билета: ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    price = Long.parseLong(line);
                    if(price>0){
                        break;
                    }else{
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    console.printError("Неправильный тип вводимых данных");
                }catch(IllegalArgumentException e){
                    console.printError("Переменная не валидна");
                }
            }
        }
        return price;
    }
    /**
     * Читает значение скидки
     */
    public static Integer askDiscount(Console console) throws AskBreak{
        int discount;
        while (true) {
            console.print("введите скидку: ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    discount=Integer.parseInt(line);
                    if(discount>0 && discount<=100){
                        break;
                    }else{
                        throw new IllegalArgumentException();
                    }
                } catch (NumberFormatException e) {
                    console.printError("Неправильный тип вводимых данных");
                }catch (IllegalArgumentException e){
                    console.printError("Переменная не валидна");
                }
            }
        }
        return discount;
    }
    /**
     * Читает тип билета
     */
    public static TicketType askTicketType(Console console) throws AskBreak{
        TicketType ticketType;
        while (true) {
            console.print("Введите тип билета(VIP,USUAL,BUDGETARY,CHEAP): ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    ticketType=TicketType.valueOf(line);
                    break;
                } catch (IllegalArgumentException e) {
                    console.printError("Неправильный тип вводимых данных");
                }
            }
        }
        return ticketType;
    }
    /**
     * Собирает введенные значения для события
     */
    public static Event askEvent(Console console) throws AskBreak {
        String name = askEventName(console);
        Long minAge = askMinAge(console);
        EventType eventType = askEventType(console);
        Event event = new Event(name, minAge, eventType);
        return event;
    }
    /**
     * Читает имя события
     */
    public static String askEventName(Console console) throws AskBreak {
        String name;
        while (true) {
            console.print("Ведите название события: ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                name = line;
                break;
            }
        }
        return name;
    }
    /**
     * Читает значение минимального возраста
     */
    public static Long askMinAge(Console console) throws AskBreak {
        long minage;
        while (true) {
            console.print("Введите минимальный возраст: ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    minage = Long.parseLong(line);
                    break;
                } catch (NumberFormatException e) {
                    console.printError("Неправильный тип вводимых данных");
                }
            }
        }
        return minage;
    }
    /**
     * Читает тип события
     */
    public static EventType askEventType(Console console) throws AskBreak {
        EventType eventType;
        while (true) {
            console.print("Введите тип события(BASEBALL,BASKETBALL,OPERA): ");
            String line = console.readln().trim();
            if(line.equals("exit")) throw new AskBreak();
            if (!line.isEmpty()) {
                try {
                    eventType=EventType.valueOf(line);
                    break;
                } catch (IllegalArgumentException e) {
                    console.printError("Неправильный тип вводимых данных");
                }
            }
        }
        return eventType;
    }

}