package com.sta.clientserver;

import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BankingServer implements Runnable {
    private final int port;
    private final Map<String, Double> accounts = new ConcurrentHashMap<>();
    private volatile boolean running = true;
    private ServerSocket serverSocket;
    private final boolean exhibitBugs;

    private final java.util.concurrent.CyclicBarrier raceBarrier = new java.util.concurrent.CyclicBarrier(2);

    public BankingServer(int port, boolean exhibitBugs) {
        this.port = port;
        this.exhibitBugs = exhibitBugs;
        accounts.put("ACC-100", 100.0);
        accounts.put("ACC-200", 500.0);
    }

    public int getPort() {
        return serverSocket != null ? serverSocket.getLocalPort() : port;
    }

    public void setBalance(String acc, double balance) {
        accounts.put(acc, balance);
    }

    public double getBalance(String acc) {
        return accounts.getOrDefault(acc, 0.0);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> handleClient(socket)).start();
                } catch (SocketException se) {
                    if (!running) break;
                }
            }
        } catch (IOException e) {
            // Server stopped or port binding error
        }
    }

    private void handleClient(Socket socket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String trimmed = line.trim();
                if ("QUIT".equalsIgnoreCase(trimmed)) {
                    writer.println("BYE");
                    break;
                }

                if (exhibitBugs && trimmed.startsWith("INVALID")) {
                    // DEFECT 2: Malformed packet unhandled exception
                    String[] badTokens = trimmed.split(" ");
                    // Bug: IndexOutOfBounds throws and drops connection abruptly
                    String cmd = badTokens[0];
                    String arg1 = badTokens[1]; // Throws ArrayIndexOutOfBoundsException!
                }

                String[] tokens = trimmed.split("\\s+");
                String command = tokens[0].toUpperCase();

                if ("BALANCE".equals(command)) {
                    if (tokens.length < 2) {
                        writer.println("ERR: Missing account ID");
                        continue;
                    }
                    Double bal = accounts.get(tokens[1]);
                    if (bal == null) {
                        writer.println("ERR: 0x80040E14 AccountNotFound"); // DEFECT: Cryptic error code
                    } else {
                        writer.println("OK: " + bal);
                    }
                } else if ("WITHDRAW".equals(command)) {
                    if (tokens.length < 3) {
                        writer.println("ERR: Usage WITHDRAW <acc> <amount>");
                        continue;
                    }
                    String acc = tokens[1];
                    double amount = Double.parseDouble(tokens[2]);

                    if (exhibitBugs) {
                        // DEFECT 1: Non-atomic withdrawal allows race-condition overdraft
                        double current = accounts.getOrDefault(acc, 0.0);
                        try {
                            raceBarrier.await(300, java.util.concurrent.TimeUnit.MILLISECONDS);
                        } catch (Exception ignored) {}
                        if (current >= amount) {
                            accounts.put(acc, accounts.get(acc) - amount); // Overdrafts to -60.0!
                            writer.println("OK: Withdrawn " + amount + " NewBalance: " + accounts.get(acc));
                        } else {
                            writer.println("ERR: Insufficient funds");
                        }
                    } else {
                        // Fixed atomic block
                        synchronized (this) {
                            double current = accounts.getOrDefault(acc, 0.0);
                            if (current >= amount) {
                                accounts.put(acc, current - amount);
                                writer.println("OK: Withdrawn " + amount + " NewBalance: " + (current - amount));
                            } else {
                                writer.println("ERR: Insufficient funds");
                            }
                        }
                    }
                } else if ("DEPOSIT".equals(command)) {
                    String acc = tokens[1];
                    double amount = Double.parseDouble(tokens[2]);
                    accounts.put(acc, accounts.getOrDefault(acc, 0.0) + amount);
                    writer.println("OK: Deposited " + amount);
                } else {
                    writer.println("ERR: Unknown command");
                }
            }
        } catch (Exception e) {
            // Thread died from uncaught exception (demonstrates defect)
        }
    }

    public void stop() {
        running = false;
        try {
            if (serverSocket != null) serverSocket.close();
        } catch (IOException ignored) {}
    }
}
