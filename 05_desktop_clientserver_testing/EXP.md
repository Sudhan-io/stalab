# Experiment 5: Client-Server & Desktop Application Testing (Functional & Usability)

## Aim
To execute test cases against a multi-threaded client–server or desktop banking application, identify functional defects (concurrency race conditions, unhandled exceptions) and usability defects (UI thread freezing, lack of confirmation prompts, cryptic error messages), analyze their severity, and prepare a detailed defect report.

---

## Algorithm / Procedure

1. **Deploy Multi-Threaded TCP Server & Client:**
   - Launch a background TCP `BankingServer` on a dynamic port managing account balances via a text-based protocol (`BALANCE`, `DEPOSIT`, `WITHDRAW`, `TRANSFER`, `QUIT`).
   - Implement `BankingClient` providing socket communication over TCP streams.
2. **Execute Functional Test Cases:**
   - **Race Condition Testing:** Spawn concurrent threads via `ExecutorService` attempting simultaneous withdrawals totaling more than the initial balance ($80 + $80 > $100) to check whether non-atomic balance updates permit negative overdrafts.
   - **Malformed Packet Testing:** Send single-token invalid payloads to verify whether the server handles errors gracefully or crashes socket worker threads.
3. **Execute Usability & Heuristic Evaluations (Nielsen Norman):**
   - **Responsiveness:** Test whether blocking network I/O executed on the desktop Event Dispatch Thread (EDT) freezes the UI.
   - **Error Prevention (Heuristic 5):** Verify whether irreversible high-value transfers ($\$50,000$) require an explicit confirmation modal before commitment.
   - **Help Users Recognize Errors (Heuristic 9):** Check whether system errors display friendly actionable explanations instead of cryptic internal hex codes (`ERR: 0x80040E14`).
4. **Compile Defect Report Matrix:**
   - Classify defects by type (Functional vs Usability), assign severity (Critical, Major, Moderate) and priority (P1, P2, P3), and log the defect report.

---

## Main Java Program

### File: `src/main/java/com/sta/clientserver/ClientServerTestRunner.java`
```java
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
            // 5. USABILITY TEST: Cryptic Error Codes
            // ==========================================
            BankingClient normalClient = new BankingClient("127.0.0.1", testPort);
            normalClient.connect();
            String resUnknown = normalClient.sendRequest("BALANCE ACC-UNKNOWN-999");
            if (resUnknown != null && resUnknown.contains("0x80040E14")) {
                defects.add(new ClientServerDefect(
                    "DEF_CS_USAB_003",
                    "USABILITY",
                    "Cryptic hex error code displayed instead of human-readable recovery message",
                    "1. Query non-existent account 'ACC-UNKNOWN-999'. 2. Inspect error prompt.",
                    "Moderate",
                    "P3",
                    "Human friendly message: 'Account not found. Please verify the account number.'",
                    "Raw internal system code displayed: " + resUnknown
                ));
            }
            normalClient.disconnect();

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

        long funcCount = defects.stream().filter(d -> "FUNCTIONAL".equalsIgnoreCase(d.getCategory())).count();
        long usabCount = defects.stream().filter(d -> "USABILITY".equalsIgnoreCase(d.getCategory())).count();

        System.out.println("--- DEFECT AUDIT METRICS ---");
        System.out.println("Total Identified Defects : " + defects.size());
        System.out.println("  * Functional Defects   : " + funcCount);
        System.out.println("  * Usability Defects    : " + usabCount);

        System.out.println("\n--- DETAILED DEFECT LOG MATRIX ---");
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
```

---

## Sample Output

```text
==========================================================================================
 STA LAB - EXPERIMENT 5: CLIENT-SERVER APPLICATION FUNCTIONAL & USABILITY TEST AUDIT
==========================================================================================
--- DEFECT AUDIT METRICS ---
Total Identified Defects : 5
  * Functional Defects   : 2
  * Usability Defects    : 3

--- DETAILED DEFECT LOG MATRIX ---
------------------------------------------------------------------------------------------
Defect ID  : DEF_CS_FUNC_001 [FUNCTIONAL] | Severity: Critical | Priority: P1
Title      : Concurrent withdrawal allows negative balance overdraft
Steps      : 1. Account has $100. 2. Two concurrent clients withdraw $80 simultaneously.
Expected   : Only first withdrawal succeeds ($20 balance); second rejected with Insufficient funds
Actual     : Both withdrawals succeeded; Final balance resulted in negative overdraft: $-60.0
------------------------------------------------------------------------------------------
Defect ID  : DEF_CS_FUNC_002 [FUNCTIONAL] | Severity: Critical | Priority: P1
Title      : Server unhandled exception terminates socket thread on malformed packet
Steps      : 1. Send single-token payload 'INVALIDMALFORMEDPAYLOAD' over TCP.
Expected   : Server returns ERR_BAD_REQUEST and keeps socket connection open
Actual     : Server threw unhandled ArrayIndexOutOfBoundsException and closed socket stream
------------------------------------------------------------------------------------------
Defect ID  : DEF_CS_USAB_001 [USABILITY] | Severity: Major | Priority: P2
Title      : Desktop Event Dispatch Thread blocked during socket I/O causing UI freeze
Steps      : 1. Enter transfer. 2. Simulate 500ms network latency. 3. Observe UI responsiveness.
Expected   : Socket I/O executed on background worker; UI renders non-blocking loading spinner
Actual     : UI thread blocked directly; window shows 'Not Responding' freezing user inputs
------------------------------------------------------------------------------------------
Defect ID  : DEF_CS_USAB_002 [USABILITY] | Severity: Major | Priority: P2
Title      : High-value money transfer executed immediately without confirmation dialog
Steps      : 1. Enter recipient account and $50,000. 2. Click 'Transfer'.
Expected   : Confirmation modal dialog: 'Confirm $50,000 transfer to recipient?'
Actual     : No confirmation prompt; immediate execution without error prevention safeguard
------------------------------------------------------------------------------------------
Defect ID  : DEF_CS_USAB_003 [USABILITY] | Severity: Moderate | Priority: P3
Title      : Cryptic hex error code displayed instead of human-readable recovery message
Steps      : 1. Query non-existent account 'ACC-UNKNOWN-999'. 2. Inspect error prompt.
Expected   : Human friendly message: 'Account not found. Please verify the account number.'
Actual     : Raw internal system code displayed: ERR: 0x80040E14 AccountNotFound
==========================================================================================
```
