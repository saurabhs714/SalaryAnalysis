package com.example.bigcompany;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeProcessorTest {

    private EmployeeProcessor processor;

    @BeforeEach
    public void setUp() {
        processor = new EmployeeProcessor();
    }

    @Test
    public void testLoadEmployees_successfulLoad() throws IOException {
        processor.loadEmployees("src/test_employees.csv");

        // Verify total employees loaded
        assertEquals(11, processor.getEmployees().size(), "Total employees loaded is incorrect.");

        // Verify CEO is correctly identified
        Employee ceo = processor.getCeo();
        assertNotNull(ceo, "CEO should not be null.");
        assertEquals(1, ceo.getId(), "CEO's ID is incorrect.");
        assertNull(ceo.getManagerId(), "CEO should not have a manager.");
    }

    @Test
    public void testSalaryAnalysis_managerEarningLess() throws IOException {
        processor.loadEmployees("src/test_employees.csv");

        // Capture console output for verification
        String output = captureConsoleOutput(() -> processor.analyzeSalaries());

        // Verify manager earning less than the minimum required
        assertTrue(output.contains("Manager 8 earns 3800.00 less than they should."),
                "Expected underpaid manager analysis result is missing.");
    }

    @Test
    public void testSalaryAnalysis_managerEarningMore() throws IOException {
        processor.loadEmployees("src/test_employees.csv");

        // Capture console output for verification
        String output = captureConsoleOutput(() -> processor.analyzeSalaries());

        // Verify manager earning more than the maximum allowed
        assertTrue(output.contains("Manager 3 earns 28500.00 more than they should."),
                "Expected overpaid manager analysis result is missing.");
    }

    @Test
    public void testReportingLines_excessiveChainLength() throws IOException {
        processor.loadEmployees("src/test_employees.csv");

        // Capture console output for verification
        String output = captureConsoleOutput(() -> processor.analyzeReportingLines());

        // Verify reporting line too long
        assertTrue(output.contains("Employee 11 is 1 levels away from the CEO."),
                "Expected reporting line analysis result is missing.");
    }

    /**
     * Utility method to capture console output during tests.
     */
    private String captureConsoleOutput(Runnable runnable) {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream originalOut = System.out;
        System.setOut(ps);
        runnable.run();
        System.out.flush();
        System.setOut(originalOut);
        return baos.toString();
    }
}
