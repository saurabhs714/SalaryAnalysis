package com.example.bigcompany;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class EmployeeProcessor {

    private Map<Integer, Employee> employees = new HashMap<>();
    private Employee ceo;

    public void loadEmployees(String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                int id = Integer.parseInt(fields[0].trim());
                String firstName = fields[1].trim();
                String lastName = fields[2].trim();
                double salary = Double.parseDouble(fields[3].trim());
                Integer managerId = fields.length > 4 && !fields[4].trim().isEmpty()
                        ? Integer.parseInt(fields[4].trim()) : null;
                Employee employee = new Employee(id, firstName, lastName, salary, managerId);
                employees.put(id, employee);
                if (managerId == null) ceo = employee;
            }
            for (Employee employee : employees.values()) {
                if (employee.getManagerId() != null) {
                    Employee manager = employees.get(employee.getManagerId());
                    manager.addSubordinate(employee);
                }
            }
        }
    }

    public void analyzeSalaries() {
        for (Employee manager : employees.values()) {
            if (!manager.getSubordinates().isEmpty()) {
                double averageSubordinateSalary = manager.getSubordinates()
                        .stream()
                        .mapToDouble(Employee::getSalary)
                        .average()
                        .orElse(0);
                double minSalary = averageSubordinateSalary * 1.2;
                double maxSalary = averageSubordinateSalary * 1.5;
                if (manager.getSalary() < minSalary) {
                    System.out.printf("Manager %d earns %.2f less than they should.%n",
                            manager.getId(), minSalary - manager.getSalary());
                } else if (manager.getSalary() > maxSalary) {
                    System.out.printf("Manager %d earns %.2f more than they should.%n",
                            manager.getId(), manager.getSalary() - maxSalary);
                }
            }
        }
    }

    public void analyzeReportingLines() {
        for (Employee employee : employees.values()) {
            int distanceToCEO = calculateDistanceToCEO(employee);
            if (distanceToCEO > 4) {
                System.out.printf("Employee %d is %d levels away from the CEO.%n",
                        employee.getId(), distanceToCEO - 4);
            }
        }
    }

    private int calculateDistanceToCEO(Employee employee) {
        int distance = 0;
        while (employee.getManagerId() != null) {
            employee = employees.get(employee.getManagerId());
            distance++;
        }
        return distance;
    }

    public static void main(String[] args) throws IOException {
        //if (args.length < 1) {
          //  System.out.println("Please provide the CSV file path as an argument.");
           // return;
        //}
        EmployeeProcessor processor = new EmployeeProcessor();
        processor.loadEmployees("src/test_employees.csv");
        processor.analyzeSalaries();
        processor.analyzeReportingLines();
    }

    public Map<Integer, Employee> getEmployees() {
        return employees;
    }

    public Employee getCeo() {
        return ceo;
    }

}
