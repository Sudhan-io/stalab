package com.sta.clientserver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ClientServerTestRunner {

    public static List<ClientServerDefect> runAudit() {
        List<ClientServerDefect> defects = new ArrayList<>();
        BankingServer server = new BankingServer(0, true);
        Thread serverThread = new Thread(server);
        serverThread.start();

        try {
            // Allow server to bind and obtain assigned port
            for (int i = 0; i < 50 && server.getPort() <= 0; i++) {
                Thread.sleep(20);
            }
            int testPort = server.getPort();

            // ==========================================
            // 1. FUNCTIONAL TEST: Concurrent Overdraft Race Condition
            // ==========================================
            server.setBalance("ACC-100", 100.0);
            ExecutorService pool = Executors.newFixedThreadPool(2);
            Callable<String> w1 = () -> {
                BankingClient c1 = new BankingClient("127.0.0.1", testPort);
                c1.connect();
                String res = c1.sendRequest("WITHDRAW ACC-100 80");
                c1.disconnect();
                return res;
            };
            Callable<String> w2 = () -> {
                BankingClient c2 = new BankingClient("127.0.0.1", testPort);
                c2.connect();
                String res = c2.sendRequest("WITHDRAW ACC-100 80");
                c2.disconnect();
                return res;
            };

            List<Future<String>> futures = pool.invokeAll(List.of(w1, w2));
            pool.shutdown();
            double finalBal = server.getBalance("ACC-100");

            if (finalBal < 0.0) {
                defects.add(new ClientServerDefect(
                    "DEF_CS_FUNC_001",
                    "FUNCTIONAL",
                    "Concurrent withdrawal allows negative balance overdraft",
                    "1. Account has $100. 2. Two concurrent clients withdraw $80 simultaneously.",
                    "Critical",
                    "P1",
                    "Only first withdrawal succeeds ($20 balance); second rejected with Insufficient funds",
                    "Both withdrawals succeeded; Final balance resulted in negative overdraft: $" + finalBal
                ));
            }

            // ==========================================
            // 2. FUNCTIONAL TEST: Malformed Packet Handling
            // ==========================================
            BankingClient badClient = new BankingClient("127.0.0.1", testPort);
            badClient.connect();
            String badResponse = badClient.sendRequest("INVALIDMALFORMEDPAYLOAD");
            // If connection closed or null returned due to uncaught exception
            if (badResponse == null) {
                defects.add(new ClientServerDefect(
                    "DEF_CS_FUNC_002",
                    "FUNCTIONAL",
                    "Server unhandled exception terminates socket thread on malformed packet",
                    "1. Send single-token payload 'INVALIDMALFORMEDPAYLOAD' over TCP.",
                    "Critical",
                    "P1",
                    "Server returns ERR_BAD_REQUEST and keeps socket connection open",
                    "Server threw unhandled ArrayIndexOutOfBoundsException and closed socket stream"
                ));
            }
            badClient.disconnect();

            // ==========================================
            // 3. USABILITY TEST: GUI Thread Freeze Under Network Latency
            // ==========================================
            BankingClient.DesktopUiSimulation uiSim = new BankingClient.DesktopUiSimulation(false, false);
            if (uiSim.doesFreezeEventDispatchThread(500)) {
                defects.add(new ClientServerDefect(
                    "DEF_CS_USAB_001",
                    "USABILITY",
                    "Desktop Event Dispatch Thread blocked during socket I/O causing UI freeze",
                    "1. Enter transfer. 2. Simulate 500ms network latency. 3. Observe UI responsiveness.",
                    "Major",
                    "P2",
                    "Socket I/O executed on background worker; UI renders non-blocking loading spinner",
                    "UI thread blocked directly; window shows 'Not Responding' freezing user inputs"
                ));
            }

            // ==========================================
            // 4. USABILITY TEST: High-Value Transfer Confirmation Modal
            // ==========================================
            if (!uiSim.hasConfirmationPromptForHighValue(50000.0)) {
                defects.add(new ClientServerDefect(
                    "DEF_CS_USAB_002",
                    "USABILITY",
                    "High-value money transfer executed immediately without confirmation dialog",
                    "1. Enter recipient account and $50,000. 2. Click 'Transfer'.",
                    "Major",
                    "P2",
                    "Confirmation modal dialog: 'Confirm $50,000 transfer to recipient?'",
                    "No confirmation prompt; immediate execution without error prevention safeguard"
                ));
            }

            // ==========================================
            // 5. USABILITY TEST: Cryptic Error Code Exposure
            // ==========================================
            BankingClient cQuery = new BankingClient("127.0.0.1", testPort);
            cQuery.connect();
            String errResponse = cQuery.sendRequest("BALANCE ACC-UNKNOWN-999");
            cQuery.disconnect();

            String friendlyCheck = uiSim.getErrorMessageFormat(errResponse);
            if (friendlyCheck.startsWith("UNFRIENDLY_HEX_CODE")) {
                defects.add(new ClientServerDefect(
                    "DEF_CS_USAB_003",
                    "USABILITY",
                    "Cryptic hex error code displayed instead of human-readable recovery message",
                    "1. Query non-existent account 'ACC-UNKNOWN-999'. 2. Inspect error prompt.",
                    "Moderate",
                    "P3",
                    "Human friendly message: 'Account not found. Please verify the account number.'",
                    "Raw internal system code displayed: " + errResponse
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }

        return defects;
    }

    public static void main(String[] args) {
        System.out.println("==========================================================================================");
        System.out.println(" STA LAB - EXPERIMENT 5: CLIENT-SERVER APPLICATION FUNCTIONAL & USABILITY TEST AUDIT");
        System.out.println("==========================================================================================");

        List<ClientServerDefect> defects = runAudit();

        long funcCount = defects.stream().filter(d -> "FUNCTIONAL".equals(d.getCategory())).count();
        long usabCount = defects.stream().filter(d -> "USABILITY".equals(d.getCategory())).count();

        System.out.println("--- DEFECT AUDIT METRICS ---");
        System.out.printf("Total Identified Defects : %d%n", defects.size());
        System.out.printf("  * Functional Defects   : %d%n", funcCount);
        System.out.printf("  * Usability Defects    : %d%n", usabCount);
        System.out.println();

        System.out.println("--- DETAILED DEFECT LOG MATRIX ---");
        for (ClientServerDefect d : defects) {
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.printf("Defect ID  : %s [%s] | Severity: %s | Priority: %s%n",
                d.getDefectId(), d.getCategory(), d.getSeverity(), d.getPriority());
            System.out.println("Title      : " + d.getTitle());
            System.out.println("Steps      : " + d.getStepsToReproduce());
            System.out.println("Expected   : " + d.getExpectedResult());
            System.out.println("Actual     : " + d.getActualResult());
        }
        System.out.println("==========================================================================================");
    }
}
