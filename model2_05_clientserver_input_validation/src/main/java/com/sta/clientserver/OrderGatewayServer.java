package com.sta.clientserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class OrderGatewayServer {

    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private volatile boolean running = false;
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();
    private final Map<String, String> orders = new ConcurrentHashMap<>();

    public OrderGatewayServer(int port) {
        this.port = port;
    }

    public synchronized void start() throws IOException {
        serverSocket = new ServerSocket(port);
        running = true;
        threadPool.submit(() -> {
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    threadPool.submit(() -> handleClient(socket));
                } catch (IOException e) {
                    if (!running) break;
                }
            }
        });
    }

    public synchronized void stop() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {}
        threadPool.shutdownNow();
    }

    public int getPort() {
        return serverSocket != null ? serverSocket.getLocalPort() : port;
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String response = processCommand(line.trim());
                writer.println(response);
            }
        } catch (Exception e) {
            // Client disconnected or socket error
        }
    }

    public String processCommand(String commandLine) {
        if (commandLine == null || commandLine.isEmpty()) {
            return "ERR 400: EMPTY_PAYLOAD";
        }

        String[] parts = commandLine.split("\\|", -1);
        String action = parts[0];

        switch (action) {
            case "PING":
                return "OK 200: PONG";

            case "AUTH":
                // Format: AUTH|username|password
                if (parts.length < 3) return "ERR 400: MALFORMED_AUTH";
                String user = parts[1];
                String pass = parts[2];
                if ("admin".equals(user) && "Secret123".equals(pass)) {
                    String token = "TOKEN-" + UUID.randomUUID().toString().substring(0, 8);
                    activeSessions.put(token, user);
                    return "OK 200: " + token;
                } else {
                    return "ERR 401: INVALID_CREDENTIALS";
                }

            case "ORDER_CREATE":
                // Format: ORDER_CREATE|token|sku|quantity|price
                if (parts.length < 5) return "ERR 400: MALFORMED_ORDER_CREATE";
                String token = parts[1];
                if (!activeSessions.containsKey(token)) {
                    return "ERR 403: UNAUTHORIZED";
                }
                String sku = parts[2];
                String qtyStr = parts[3];
                String priceStr = parts[4];

                try {
                    int qty = Integer.parseInt(qtyStr);
                    double price = Double.parseDouble(priceStr);

                    if (qty <= 0) {
                        return "ERR 422: INVALID_QUANTITY_NEGATIVE";
                    }

                    // BUG (Input-Validation Defect):
                    // Fails to validate maximum integer limit (e.g. qty > 10,000)
                    // Allows 999999999 causing inventory corruption

                    String orderId = "ORD-" + System.currentTimeMillis();
                    orders.put(orderId, "SKU=" + sku + ", Qty=" + qty + ", Price=" + price);
                    return "OK 201: " + orderId;
                } catch (NumberFormatException e) {
                    return "ERR 422: NUMERIC_PARSE_ERROR";
                }

            case "ORDER_STATUS":
                // Format: ORDER_STATUS|token|orderId
                // BUG (Functional Defect):
                // Server neglects to verify token authentication before revealing order details!
                if (parts.length < 3) return "ERR 400: MALFORMED_ORDER_STATUS";
                String targetOrder = parts[2];
                if (orders.containsKey(targetOrder)) {
                    return "OK 200: " + orders.get(targetOrder);
                } else {
                    return "ERR 404: ORDER_NOT_FOUND";
                }

            case "CRASH_UNKNOWN":
                // BUG (Error-Handling Defect):
                // Instead of gracefully returning ERR 400, it throws unhandled NullPointerException
                String nullPointer = null;
                return nullPointer.toUpperCase();

            default:
                return "ERR 405: UNKNOWN_ACTION";
        }
    }
}

