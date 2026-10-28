package com.sta.clientserver;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ClientServerValidationTest {

    private static OrderGatewayServer server;
    private static OrderGatewayClient client;

    @BeforeAll
    public static void startServer() throws IOException {
        server = new OrderGatewayServer(0);
        server.start();
        client = new OrderGatewayClient("127.0.0.1", server.getPort());
    }

    @AfterAll
    public static void stopServer() {
        if (server != null) server.stop();
    }

    @Test
    public void testServerExecutionAndDefectDetection() {
        ClientServerTestSuite suite = new ClientServerTestSuite();
        List<ClientServerTestSuite.TestCaseResult> results = suite.runAllTests(client);

        assertEquals(10, results.size(), "Should execute exactly 10 test cases");

        long passed = results.stream().filter(r -> r.passed).count();
        long failed = results.stream().filter(r -> !r.passed).count();

        assertEquals(7, passed, "7 tests should pass cleanly");
        assertEquals(3, failed, "3 tests should fail and flag realistic functional/input/error defects with evidence");
    }
}
