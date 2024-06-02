package com.f4d4.general.facility;

import com.f4d4.general.tools.Validatable;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;



import java.util.Objects;

/**
 * Класс События
 */

public class Event implements Validatable  , Serializable {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private long minAge; //Поле не может быть null
    private EventType eventType; //Поле не может быть null



    public Event(long id , String name , long minAge , EventType eventType){
        this.id = id;
        this.name = name;
        this.minAge = minAge;
        this.eventType = eventType;
    }

    public Event(String name , long minAge , EventType eventType){
        this.name = name;
        this.minAge = minAge;
        this.eventType = eventType;
    }

    @Override
    public String toString(){
        String conclusion = "";
        conclusion+= "ID = " + id + " | ";
        conclusion+= "Название собития = " + name + " | ";
        conclusion+= "Минимальный восзраст = " + minAge + " | ";
        conclusion+= "Тип события = " + eventType + " | ";
        return conclusion;
    }

    /**
     * Проверяет валидность полей класса
     */
    @Override
    public boolean validate(){
        if (id <= 0) return false;
        if (name == null || name.isEmpty()) return false;
        if(eventType==null) return false;
        return true;
    }
    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public long getMinAge(){
        return minAge;
    }

    public EventType getEventType(){
        return eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name)
                && Objects.equals(minAge, event.minAge) && Objects.equals(eventType, event.eventType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, minAge, eventType);
    }
}