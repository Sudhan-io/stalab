# Client-Server / Desktop Application Defect Analysis Report

**Target System:** Core Banking Client-Server Desktop Application (Swing Client + Multi-Threaded TCP Socket Server)  
**Document ID:** DEF-REP-CS-2026-V1.0  
**Testing Scope:** Functional Verification & Usability Audit (Nielsen Norman 10 Usability Heuristics)  

---

## 1. Executive Summary
During execution of test cycles against the Client-Server Banking System, test cases identified both **Functional (backend / protocol integrity)** and **Usability (user experience / desktop interface)** defects.

* **Total Defects Logged:** 6
  - **Functional Defects:** 3
  - **Usability Defects:** 3
* **Severity Breakdown:**
  - **Critical (P1):** 2 (Concurrent overdraft race condition; Server crash on malformed packet)
  - **Major (P2):** 2 (Desktop UI freeze on network lag; Irreversible high-value transfer without confirmation dialog)
  - **Moderate (P3):** 1 (Cryptic error codes without human-readable recovery guidance)
  - **Minor (P4):** 1 (Lack of shortcut keys and visual tab traversal order inconsistency)

---

## 2. Functional Defect Log

| Defect ID | Title & Summary | Steps to Reproduce | Severity / Priority | Expected Result | Actual Result |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DEF_CS_FUNC_001** | Concurrent withdrawal allows negative balance overdraft | 1. Initialize Account ACC-100 with \$100.<br>2. Dispatch two simultaneous withdrawal requests of \$80 each via concurrent client sockets. | **Critical** / **P1** | Only first withdrawal succeeds; second is rejected with "Insufficient balance". Final balance = \$20. | Both withdrawals succeed due to lack of thread synchronization; balance overdrafts to -\$60. |
| **DEF_CS_FUNC_002** | Server unhandled NullPointer terminates socket thread on malformed packet | 1. Open TCP socket to server port 9090.<br>2. Send arbitrary malformed payload `INVALID_RAW_DATA\n`. | **Critical** / **P1** | Server validates packet format, logs warning, returns `ERR_BAD_REQUEST`, and keeps socket connection alive. | Server throws unhandled `ArrayIndexOutOfBoundsException`, terminates client handler thread abruptly, closing connection. |
| **DEF_CS_FUNC_003** | Server fails to release client socket descriptors upon abrupt client disconnect | 1. Client connects to server.<br>2. Client process killed forcefully (`kill -9` / Task Manager).<br>3. Inspect server open file descriptors. | **Major** / **P2** | Server detects TCP socket EOF / FIN, triggers `finally` block, closes socket, and decrements active connection count. | Socket remains in `CLOSE_WAIT` state indefinitely; causes file descriptor exhaustion over prolonged uptime. |

---

## 3. Usability Defect Log (Heuristic Analysis)

| Defect ID | Usability Issue | Heuristic Violated | Severity / Priority | Steps to Reproduce & Impact | Expected UX Design |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **DEF_CS_USAB_001** | UI Event Dispatch Thread (EDT) blocks during network calls causing desktop app freeze | **Visibility of System Status (Heuristic #1)** | **Major** / **P2** | 1. Enter \$5,000 transfer.<br>2. Click 'Transfer Funds'.<br>3. During 3-second network latency, UI window turns white / "Not Responding". User clicks repeatedly, causing duplicate requests. | Network call executed on background worker (`SwingWorker`); UI displays progress spinner or modal "Processing Transfer..." bar. |
| **DEF_CS_USAB_002** | High-value money transfer executed immediately without confirmation modal | **Error Prevention (Heuristic #5)** | **Major** / **P2** | 1. Type account `ACC-999` and amount `$50,000`.<br>2. Click 'Submit'.<br>3. Transfer executes instantly without confirmation or review dialog. Accidental clicks cannot be undone. | Modal confirmation dialog: "Are you sure you want to transfer \$50,000 to ACC-999? [Confirm] [Cancel]". |
| **DEF_CS_USAB_003** | Obscure hex error codes displayed to end user | **Help Users Recognize, Diagnose, & Recover from Errors (Heuristic #9)** | **Moderate** / **P3** | 1. Enter invalid recipient account number `ACC-EMPTY`.<br>2. Click 'Transfer'.<br>3. Dialog displays `Error: 0x80040E14 Database Exception`. | Friendly error message: "Account number not found. Please double-check recipient details." |

---

## 4. Root Cause Analysis & Recommendations
1. **Thread Safety:** Implement mutex / atomic balance transactions using `synchronized` blocks or `ReentrantLock` around account balance modification methods.
2. **Robust Protocol Parser:** Wrap packet tokenizers in schema validation blocks returning structured error payloads rather than raising uncaught runtime exceptions.
3. **Responsive Architecture:** Shift all network socket I/O off Swing Event Dispatch Thread (EDT) into asynchronous tasks (`CompletableFuture` / `SwingWorker`).
4. **UX Guardrails:** Add confirmation prompts on destructive operations and replace raw exception dumps with user-friendly messages.

