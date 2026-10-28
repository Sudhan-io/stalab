package com.sta.clientserver;

import java.io.*;
import java.net.*;

public class OrderGatewayClient {

    private final String host;
    private final int port;

    public OrderGatewayClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String sendRequest(String payload) {
        try (Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            socket.setSoTimeout(3000);
            writer.println(payload);
            String response = reader.readLine();
            return response != null ? response : "DISCONNECTED_BY_SERVER";
        } catch (SocketTimeoutException e) {
            return "ERR_TIMEOUT";
        } catch (IOException e) {
            return "ERR_CONNECTION_FAILED: " + e.getMessage();
        }
    }
}

