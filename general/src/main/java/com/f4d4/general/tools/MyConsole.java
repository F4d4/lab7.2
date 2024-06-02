package com.f4d4.general.tools;

import java.util.Scanner;
/**
 * Класс реализации методов интерфеса консоли
 */
public class MyConsole implements Console{
    private static final String P = "$ ";
    private static Scanner fileScanner = null;
    private static Scanner defScanner = new Scanner(System.in);
    /**
     * консоль делает System.out.println(obj)
     */
    @Override
    public void println(Object obj){
        System.out.println(obj);
    }
    /**
     * консоль делает System.out.print(obj)
     */
    @Override
    public void print(Object obj){
        System.out.print(obj);
    }
    /**
     * консоль считывает команду которую мы вводим
     */
    @Override
    public String readln() {
        if (fileScanner != null){
            return fileScanner.nextLine();
        }
        else {
            return defScanner.nextLine();
        }
    }
    /**
     * консоль выводит ошибку
     */
    @Override
    public void printError(Object obj){
        System.out.println(obj);
    }

    @Override
    public void prompt() {
        print(P);
    }

    @Override
    public String getPrompt() {
        return P;
    }
    /**
     * консоль выводит две колонки с командой и пояснением к ней
     */
    @Override
    public void printTable(Object elementLeft, Object elementRight) {
        System.out.printf(" %-35s%-1s%n", elementLeft, elementRight);
    }

    @Override
    public boolean isCanReadln() throws IllegalStateException {
        return (fileScanner!=null?fileScanner:defScanner).hasNextLine();
    }

    @Override
    public void selectFileScanner(Scanner scanner) {
        this.fileScanner = scanner;
    }

    @Override
    public void selectConsoleScanner() {
        this.fileScanner = null;
    }



}
