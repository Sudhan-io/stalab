# Experiment 05 (Model 2)

## Aim
To test a client–server or desktop application by executing functional, input-validation, and error-handling test cases, evaluate the test results, and document the identified defects with suitable evidence.

## Algorithm / Procedure
1. Implement a client-server socket architecture where `OrderGatewayServer` binds to a dynamic local port and processes pipe-delimited request commands.
2. Formulate test cases categorized into:
   - **Functional**: Connectivity check, credential authorization, order processing, and token verification.
   - **Input-Validation**: Type checking, non-negative quantity limits, and boundary range enforcement.
   - **Error-Handling**: Handling blank packets, illegal protocol verbs, and unhandled server runtime exceptions.
3. Establish socket connections via `OrderGatewayClient` with a timeout mechanism.
4. Execute each request payload over the TCP socket, read the response, and evaluate status against Expected Result specifications.
5. If a test fails or server behavior deviates from standard specification, capture the exact payload, received raw response, and diagnosis as technical defect evidence.
6. Compile all test results and defect cards into an analytical report.

## Main Java Program
```java
package com.sta.clientserver;

import java.util.*;

public class ClientServerTestSuite {

    public static class TestCaseResult {
        public String id;
        public String category;
        public String testName;
        public String requestPayload;
        public String expectedResponse;
        public String actualResponse;
        public boolean passed;
        public String defectEvidence;

        public TestCaseResult(String id, String category, String testName, String requestPayload,
                              String expectedResponse, String actualResponse, boolean passed, String defectEvidence) {
            this.id = id;
            this.category = category;
            this.testName = testName;
            this.requestPayload = requestPayload;
            this.expectedResponse = expectedResponse;
            this.actualResponse = actualResponse;
            this.passed = passed;
            this.defectEvidence = defectEvidence;
        }
    }

    public List<TestCaseResult> runAllTests(OrderGatewayClient client) {
        List<TestCaseResult> results = new ArrayList<>();

        // Functional Tests
        String resPing = client.sendRequest("PING");
        results.add(new TestCaseResult("TC-F01", "Functional", "Server Ping Healthcheck", "PING",
                "OK 200: PONG", resPing, "OK 200: PONG".equals(resPing), null));

        String resAuth = client.sendRequest("AUTH|admin|Secret123");
        boolean passAuth = resAuth != null && resAuth.startsWith("OK 200: TOKEN-");
        String validToken = passAuth ? resAuth.substring(8).trim() : "INVALID_TOKEN";
        results.add(new TestCaseResult("TC-F02", "Functional", "Admin User Authentication", "AUTH|admin|Secret123",
                "OK 200: TOKEN-*", resAuth, passAuth, null));

        String resOrder = client.sendRequest("ORDER_CREATE|" + validToken + "|SKU-IPHONE|1|999.00");
        boolean passOrder = resOrder != null && resOrder.startsWith("OK 201: ORD-");
        String createdOrderId = passOrder ? resOrder.substring(8).trim() : "ORD-0";
        results.add(new TestCaseResult("TC-F03", "Functional", "Standard Order Creation", "ORDER_CREATE|TOKEN|SKU-IPHONE|1|999.00",
                "OK 201: ORD-*", resOrder, passOrder, null));

        String resUnauthStatus = client.sendRequest("ORDER_STATUS|FAKE_OR_EMPTY_TOKEN|" + createdOrderId);
        boolean passUnauth = resUnauthStatus.startsWith("ERR 403") || resUnauthStatus.startsWith("ERR 401");
        String defectF04 = !passUnauth ? "EVIDENCE: Server returned private order details without validating token: " + resUnauthStatus : null;
        results.add(new TestCaseResult("TC-F04", "Functional", "Order Status Token Authorization Check",
                "ORDER_STATUS|FAKE_OR_EMPTY_TOKEN|" + createdOrderId,
                "ERR 403: UNAUTHORIZED", resUnauthStatus, passUnauth, defectF04));

        // Input-Validation Tests
        String resNegQty = client.sendRequest("ORDER_CREATE|" + validToken + "|SKU-IPHONE|-5|999.00");
        results.add(new TestCaseResult("TC-V01", "Input-Validation", "Reject Negative Quantity", "ORDER_CREATE|...|-5|999.00",
                "ERR 422: INVALID_QUANTITY_NEGATIVE", resNegQty, "ERR 422: INVALID_QUANTITY_NEGATIVE".equals(resNegQty), null));

        String resAlphaQty = client.sendRequest("ORDER_CREATE|" + validToken + "|SKU-IPHONE|ABC|999.00");
        results.add(new TestCaseResult("TC-V02", "Input-Validation", "Reject Non-Numeric Quantity", "ORDER_CREATE|...|ABC|999.00",
                "ERR 422: NUMERIC_PARSE_ERROR", resAlphaQty, "ERR 422: NUMERIC_PARSE_ERROR".equals(resAlphaQty), null));

        String resHugeQty = client.sendRequest("ORDER_CREATE|" + validToken + "|SKU-IPHONE|999999999|999.00");
        boolean passHugeQty = resHugeQty.startsWith("ERR 422") || resHugeQty.contains("EXCEEDS_MAX_LIMIT");
        String defectV03 = !passHugeQty ? "EVIDENCE: Server permitted purchase of 999,999,999 units without business boundary limit. Response: " + resHugeQty : null;
        results.add(new TestCaseResult("TC-V03", "Input-Validation", "Reject Out-of-Bound Extreme Quantity", "ORDER_CREATE|...|999999999|999.00",
                "ERR 422: EXCEEDS_MAX_LIMIT", resHugeQty, passHugeQty, defectV03));

        // Error-Handling Tests
        String resEmpty = client.sendRequest("");
        results.add(new TestCaseResult("TC-E01", "Error-Handling", "Handle Empty Socket Payload", "",
                "ERR 400: EMPTY_PAYLOAD", resEmpty, "ERR 400: EMPTY_PAYLOAD".equals(resEmpty), null));

        String resUnknown = client.sendRequest("UNKNOWN_VERB_XYZ");
        results.add(new TestCaseResult("TC-E02", "Error-Handling", "Handle Unsupported Verb", "UNKNOWN_VERB_XYZ",
                "ERR 405: UNKNOWN_ACTION", resUnknown, "ERR 405: UNKNOWN_ACTION".equals(resUnknown), null));

        String resCrash = client.sendRequest("CRASH_UNKNOWN");
        boolean passCrash = resCrash.startsWith("ERR 500") || resCrash.startsWith("ERR 400");
        String defectE03 = !passCrash ? "EVIDENCE: Server crashed with NullPointerException; abruptly severed TCP connection. Response: " + resCrash : null;
        results.add(new TestCaseResult("TC-E03", "Error-Handling", "Graceful Exception Handling on Internal Error", "CRASH_UNKNOWN",
                "ERR 500: INTERNAL_ERROR", resCrash, passCrash, defectE03));

        return results;
    }

    public static void main(String[] args) throws Exception {
        OrderGatewayServer server = new OrderGatewayServer(0);
        server.start();
        int port = server.getPort();
        System.out.println("Started Test Server on localhost:" + port);

        OrderGatewayClient client = new OrderGatewayClient("127.0.0.1", port);
        ClientServerTestSuite suite = new ClientServerTestSuite();
        List<TestCaseResult> results = suite.runAllTests(client);

        server.stop();

        System.out.println("==========================================================================================");
        System.out.println("       CLIENT-SERVER APPLICATION TEST EXECUTION REPORT (FUNCTIONAL, INPUT, ERROR)         ");
        System.out.println("==========================================================================================");
        System.out.printf("%-8s | %-16s | %-32s | %-8s\n", "TC ID", "CATEGORY", "SCENARIO DESCRIPTION", "STATUS");
        System.out.println("------------------------------------------------------------------------------------------");

        int passCount = 0;
        int failCount = 0;
        List<TestCaseResult> defects = new ArrayList<>();

        for (TestCaseResult r : results) {
            String status = r.passed ? "PASSED" : "FAILED";
            if (r.passed) passCount++;
            else {
                failCount++;
                defects.add(r);
            }
            System.out.printf("%-8s | %-16s | %-32s | %-8s\n", r.id, r.category, r.testName, status);
        }

        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Total Tests Executed : " + results.size());
        System.out.println("Tests Passed         : " + passCount);
        System.out.println("Tests Failed (Defects): " + failCount);
        System.out.println("==========================================================================================\n");

        System.out.println("[DOCUMENTED DEFECTS WITH EVIDENCE]");
        for (TestCaseResult d : defects) {
            System.out.println("------------------------------------------------------------------------------------------");
            System.out.println("Defect ID           : DEF-" + d.id);
            System.out.println("Classification      : " + d.category);
            System.out.println("Scenario            : " + d.testName);
            System.out.println("Request Payload Sent: " + d.requestPayload);
            System.out.println("Expected Response   : " + d.expectedResponse);
            System.out.println("Actual Response     : " + d.actualResponse);
            System.out.println("Technical Evidence  : " + d.defectEvidence);
        }
        System.out.println("==========================================================================================");
    }
}
```

## Sample Output
```text
Started Test Server on localhost:4278
==========================================================================================
       CLIENT-SERVER APPLICATION TEST EXECUTION REPORT (FUNCTIONAL, INPUT, ERROR)         
==========================================================================================
TC ID    | CATEGORY         | SCENARIO DESCRIPTION             | STATUS  
------------------------------------------------------------------------------------------
TC-F01   | Functional       | Server Ping Healthcheck          | PASSED  
TC-F02   | Functional       | Admin User Authentication        | PASSED  
TC-F03   | Functional       | Standard Order Creation          | PASSED  
TC-F04   | Functional       | Order Status Token Authorization Check | FAILED  
TC-V01   | Input-Validation | Reject Negative Quantity         | PASSED  
TC-V02   | Input-Validation | Reject Non-Numeric Quantity      | PASSED  
TC-V03   | Input-Validation | Reject Out-of-Bound Extreme Quantity | FAILED  
TC-E01   | Error-Handling   | Handle Empty Socket Payload      | PASSED  
TC-E02   | Error-Handling   | Handle Unsupported Verb          | PASSED  
TC-E03   | Error-Handling   | Graceful Exception Handling on Internal Error | FAILED  
------------------------------------------------------------------------------------------
Total Tests Executed : 10
Tests Passed         : 7
Tests Failed (Defects): 3
==========================================================================================

[DOCUMENTED DEFECTS WITH EVIDENCE]
------------------------------------------------------------------------------------------
Defect ID           : DEF-TC-F04
Classification      : Functional
Scenario            : Order Status Token Authorization Check
Request Payload Sent: ORDER_STATUS|FAKE_OR_EMPTY_TOKEN|ORD-1793165183122
Expected Response   : ERR 403: UNAUTHORIZED
Actual Response     : OK 200: SKU=SKU-IPHONE, Qty=1, Price=999.0
Technical Evidence  : EVIDENCE: Server returned private order details without validating token: OK 200: SKU=SKU-IPHONE, Qty=1, Price=999.0
------------------------------------------------------------------------------------------
Defect ID           : DEF-TC-V03
Classification      : Input-Validation
Scenario            : Reject Out-of-Bound Extreme Quantity
Request Payload Sent: ORDER_CREATE|...|999999999|999.00
Expected Response   : ERR 422: EXCEEDS_MAX_LIMIT
Actual Response     : OK 201: ORD-1793165183134
Technical Evidence  : EVIDENCE: Server permitted purchase of 999,999,999 units without business boundary limit. Response: OK 201: ORD-1793165183134
------------------------------------------------------------------------------------------
Defect ID           : DEF-TC-E03
Classification      : Error-Handling
Scenario            : Graceful Exception Handling on Internal Error
Request Payload Sent: CRASH_UNKNOWN
Expected Response   : ERR 500: INTERNAL_ERROR
Actual Response     : DISCONNECTED_BY_SERVER
Technical Evidence  : EVIDENCE: Server crashed with NullPointerException; abruptly severed TCP connection. Response: DISCONNECTED_BY_SERVER
==========================================================================================
```

