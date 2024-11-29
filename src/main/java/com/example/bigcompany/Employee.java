package com.example.bigcompany;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private Integer managerId;
    private List<Employee> subordinates;

    public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
        this.subordinates = new ArrayList<>();
    }

    public int getId() { return id; }
    public double getSalary() { return salary; }
    public Integer getManagerId() { return managerId; }
    public List<Employee> getSubordinates() { return subordinates; }
    public void addSubordinate(Employee subordinate) { subordinates.add(subordinate); }
}
