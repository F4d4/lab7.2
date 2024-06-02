package com.f4d4.general.facility;

import com.f4d4.general.tools.Validatable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Класс Координат
 */
public class Coordinates implements Validatable , Serializable {
    private Integer x; //Значение поля должно быть больше -395, Поле не может быть null
    private Double y; //Максимальное значение поля: 99, Поле не может быть null

    public Coordinates(int x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return x + ";" + y;
    }

    /**
     * Проверяет валидность координат
     */
    @Override
    public boolean validate() {
        if (x == null || y == null) return false;
        return x>-395 && y<=99;
    }

    public int getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Coordinates that = (Coordinates) obj;
        return Objects.equals(x,that.x)&&Objects.equals(y,that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x,y);
    }
}