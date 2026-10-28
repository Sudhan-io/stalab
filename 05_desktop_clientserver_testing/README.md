# Experiment 05: Client-Server / Desktop Application Testing

## Question
> Execute test cases against a client–server or desktop application. Identify functional and usability defects, analyze their severity, and prepare a detailed defect report.

---

## 📝 Main Java File(s) to Write in Lab Exam / Practical
In your lab exam, you need to provide the client-server defect analysis report and the client-server socket audit application:
* **Documentation File:** `DESKTOP_CLIENTSERVER_DEFECT_REPORT.md` (Detailed defect report categorizing Functional defects like race condition overdrafts, and Usability defects according to Nielsen Norman heuristics)
* **Main Java Class to Write:** [`src/main/java/com/sta/clientserver/ClientServerTestRunner.java`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/src/main/java/com/sta/clientserver/ClientServerTestRunner.java)
  * **Class Name:** `com.sta.clientserver.ClientServerTestRunner`
  * **Function:** Boots the multi-threaded socket server, dispatches concurrent client requests, evaluates race conditions and UI thread blocks, and prints the defect matrix.
* **Underlying Socket Server & Client:**
  - [`src/main/java/com/sta/clientserver/BankingServer.java`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/src/main/java/com/sta/clientserver/BankingServer.java)
  - [`src/main/java/com/sta/clientserver/BankingClient.java`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/src/main/java/com/sta/clientserver/BankingClient.java)
* **Automated JUnit 5 Test Suite:** [`src/test/java/com/sta/clientserver/ClientServerFunctionalAndUsabilityTest.java`](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/src/test/java/com/sta/clientserver/ClientServerFunctionalAndUsabilityTest.java)

---

## Deliverables in this Directory
1. **[DESKTOP_CLIENTSERVER_DEFECT_REPORT.md](file:///d:/PROJECTS/stalab/05_desktop_clientserver_testing/DESKTOP_CLIENTSERVER_DEFECT_REPORT.md)** - Formal defect analysis report covering both Functional & Usability (Nielsen Norman Heuristics) defects, severity classification, and root causes.
2. **`src/main/java/com/sta/clientserver/`** -
   - `BankingServer.java`: Multi-threaded TCP socket server demonstrating transaction handling.
   - `BankingClient.java`: Socket client and desktop UI interaction simulator.
   - `ClientServerDefect.java`: Standard defect model.
   - `ClientServerTestRunner.java`: Automated audit harness that boots the server, fires concurrent test commands, checks UI responsiveness & error handling, and formats the defect report.
3. **`src/test/java/com/sta/clientserver/ClientServerFunctionalAndUsabilityTest.java`** - Automated JUnit 5 tests.
4. **`pom.xml`** - Self-contained Maven configuration.

---

## Prerequisites (Setting Up on Any New Computer)
1. **Java JDK (17 or higher)** (`java -version`)
2. **Apache Maven (3.8 or higher)** (`mvn -version`)

---

## How to Build and Run

Open terminal/PowerShell inside this folder:
```bash
cd 05_desktop_clientserver_testing
```

### 1. Run Automated Defect Verification Tests
```bash
mvn clean test
```

### 2. Run Client-Server Audit & Defect Report Generator
```bash
mvn compile exec:java
```

---

## Expected Output
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
...
Defect ID  : DEF_CS_USAB_001 [USABILITY] | Severity: Major | Priority: P2
Title      : Desktop Event Dispatch Thread blocked during socket I/O causing UI freeze
...
==========================================================================================
```
