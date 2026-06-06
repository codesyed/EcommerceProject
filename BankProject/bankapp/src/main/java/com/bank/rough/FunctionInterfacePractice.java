package com.bank.rough;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
class Student {
    String name;
    double percentage;

    public Student(String name, double percentage) {
        this.name = name;
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public double getPercentage() {
        return percentage;
    }
}

public class FunctionInterfacePractice {

    public static void main(String[] args) {

         Optional<String> opNull = Optional.ofNullable(null);
         Optional<String> opFull = Optional.ofNullable("abcd");

         opFull.orElseThrow(()->new ArithmeticException());
         opNull.orElseThrow(()->new RuntimeException());
         //      .orElseThrow(Consumer)  //


    }



}
