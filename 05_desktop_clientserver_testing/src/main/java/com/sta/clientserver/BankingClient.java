package com.sta.clientserver;

import java.io.*;
import java.net.Socket;

public class BankingClient {
    private final String host;
    private final int port;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public BankingClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public String sendRequest(String command) throws IOException {
        if (writer == null) {
            throw new IOException("Not connected to server");
        }
        writer.println(command);
        return reader.readLine();
    }

    public void disconnect() {
        try {
            if (writer != null) writer.println("QUIT");
            if (socket != null) socket.close();
        } catch (IOException ignored) {}
    }

    // Usability Simulation Checks
    public static class DesktopUiSimulation {
        private final boolean showConfirmationModal;
        private final boolean runOnBackgroundWorker;

        public DesktopUiSimulation(boolean showConfirmationModal, boolean runOnBackgroundWorker) {
            this.showConfirmationModal = showConfirmationModal;
            this.runOnBackgroundWorker = runOnBackgroundWorker;
        }

        public boolean hasConfirmationPromptForHighValue(double amount) {
            return amount > 1000.0 && showConfirmationModal;
        }

        public boolean doesFreezeEventDispatchThread(int networkLatencyMs) {
            // If runOnBackgroundWorker is false, network latency directly locks GUI thread
            return !runOnBackgroundWorker && networkLatencyMs > 200;
        }

        public String getErrorMessageFormat(String rawServerResponse) {
            if (rawServerResponse != null && rawServerResponse.contains("0x80040E14")) {
                return "UNFRIENDLY_HEX_CODE: " + rawServerResponse;
            }
            return "FRIENDLY: " + rawServerResponse;
        }
    }
}

