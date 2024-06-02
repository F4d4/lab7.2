package com.f4d4.client;

import com.f4d4.client.managers.SocketClient;
import com.f4d4.client.tools.Ask;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws Exception {
        SocketClient client = new SocketClient("localhost",8080);
        client.start();
    }
}
