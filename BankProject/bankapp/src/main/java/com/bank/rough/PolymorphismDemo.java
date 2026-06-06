package com.bank.rough;

// Base class
abstract class Employee {
    protected String name;
    public Employee(String name) {
        this.name = name;
    }
    public abstract double calculateBonus(double salary);
}

// Subclass 1 - Developer
class Developer extends Employee {
    public Developer(String name) {
        super(name);
    }
    @Override
    public double calculateBonus(double salary) {
        return salary * 0.10;  // Developers get 10% bonus
    }
}

// Subclass 2 - Manager
class Manager extends Employee {
    public Manager(String name) {
        super(name);
    }
    @Override
    public double calculateBonus(double salary) {
        return salary * 0.20;  // Managers get 20% bonus
    }
}

// Demonstrating polymorphism
public class PolymorphismDemo {
    public static void main(String[] args) {
        Employee dev = new Developer("Alice");
        Employee manager = new Manager("Bob");

        double devBonus = dev.calculateBonus(100000);
        double managerBonus = manager.calculateBonus(100000);

        System.out.println("Alice’s bonus: $" + devBonus);      // Alice’s bonus: $10,000.0
        System.out.println("Bob’s bonus: $" + managerBonus);   // Bob’s bonus: $20,000.0
    }
}