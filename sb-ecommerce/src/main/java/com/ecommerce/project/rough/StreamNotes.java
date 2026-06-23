package com.ecommerce.project.rough;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

class Book {
    private String name;
    private String category;

    public Book(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "\n Book{name='" + name + "', category='" + category + "'}";
    }
}

public class StreamNotes {
    public static void main(String[] args) {
        List<Book>list = new ArrayList<>();
        list.add(new Book("Effective Java", "Programming"));
        list.add(new Book("Spring in Action", "Programming"));
        list.add(new Book("Design Patterns", "Software Design"));
        list.add(new Book("Atomic Habits", "Self Help"));

        System.out.println(list);

        /* Syntax: list.removeIf(Predicate<? Book> filter) */
        /* removing all Programming books if present */
        list.removeIf(book->book.getCategory().equalsIgnoreCase("programming"));

        System.out.println(list);
    }
}