package com.tradingsim.client.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkHelper {
    public static void test(String args) throws IOException {
        Socket socket = new Socket("localhost", 9090);

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        out.println("Hello from client!");
        String response = in.readLine();
        System.out.println("Server says: " + response);

        socket.close();
    }
}
