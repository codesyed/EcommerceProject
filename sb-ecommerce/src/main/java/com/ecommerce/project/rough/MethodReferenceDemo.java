package com.ecommerce.project.rough;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MethodReferenceDemo {

    public static void main(String[] args) {
    /*
    🔥 INTERFACE OBJECT CREATION (REFERENCE NOTES)
    ❌ We CANNOT create object of an interface directly
    👉 WRONG: A obj = new A();  // ❌ Not allowed
    ✅ HOW WE CAN USE INTERFACE
    ✔ Interface reference CAN point to object of:
    1) Implementation Class
    2) Anonymous Class
    3) Lambda Expression (Functional Interface)

    🔹 1. Using Implementation Class
    interface A {
        void show();
    }
    class B implements A {
        public void show() {
            System.out.println("Hello from Class");
        }
    }
    A obj1 = new B();  // ✔ Allowed
    ------------------------------------------------------------
    🔹 2. Using Anonymous Class
    ------------------------------------------------------------
    A obj2 = new A() {
        public void show() {
            System.out.println("Hello from Anonymous Class");
        }
    };
    ------------------------------------------------------------
    🔹 3. Using Lambda Expression (Java 8+)
    ------------------------------------------------------------
    // Only if interface is FUNCTIONAL (one abstract method)
    A obj3 = () -> System.out.println("Hello from Lambda");

    Interface ka object directly nahi banta ❌
    But uska reference kisi implementation ko point karta hai ✔

    A obj → reference
    new B() / anonymous / lambda → actual object
*/
        // Static Method Reference vs Lambda
        Function<Integer, Double> lambda1 = (n) -> Math.sqrt(n);
        Function<Integer, Double> methodRef1 = Math::sqrt;

        System.out.println("Lambda sqrt: " + lambda1.apply(25));
        System.out.println("MethodRef sqrt: " + methodRef1.apply(25));

        // Instance Method of a Particular Object
        String msg = "Hello World";
        Supplier<Integer> lambda2 = () -> msg.length();
        Supplier<Integer> methodRef2 = msg::length;

        System.out.println("\nLambda length: " + lambda2.get());
        System.out.println("MethodRef length: " + methodRef2.get());

        // Instance Method of a Class Type
        List<String> names = List.of("Arnold", "Zarvis", "Mathew");

        System.out.println("\nLambda print:");
        names.forEach(name -> System.out.println(name));

        System.out.println("\nMethodRef print:");
        names.forEach(System.out::println);

        // Constructor Reference
//        Supplier<Employee> lambda4 = () -> new Employee();
//        Supplier<Employee> methodRef4 = Employee::new;

        System.out.println("\nLambda creating Employee:");
//        lambda4.get();

        System.out.println("\nMethodRef creating Employee:");
//        methodRef4.get();
        List<Integer>li = new ArrayList<>();
        li.add(1);
        li.add(2);
        li.forEach(a-> System.out.println(a));
    }
}
