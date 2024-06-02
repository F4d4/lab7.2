package com.f4d4.general.tools;

import java.util.Scanner;
/**
 * Консоль для ввода команд
 */
public interface Console {
    void println(Object obj);
    void print(Object obj);
    String readln();
    void printError(Object obj);
    void prompt();
    void printTable(Object obj1, Object obj2);
    boolean isCanReadln();
    void selectFileScanner(Scanner obj);
    void selectConsoleScanner();

    String getPrompt();
}
