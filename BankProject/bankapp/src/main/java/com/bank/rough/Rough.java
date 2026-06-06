package com.bank.rough;


import org.springframework.cglib.core.Local;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.Month;

public class Rough {
    public static void main(String[] args) {
        //DateTimeFormatter
        DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyy/MM/dd<->HH/mm/ss");
        LocalDateTime ldt1 = LocalDateTime.parse("2023/04/22<->02/54/59", dtf1);
        System.out.println(ldt1);

        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("H,mm,ss<>yy/MM/dd");
        LocalDateTime ldt2 = LocalDateTime.parse("5,59,54<>26/12/22", dtf2);
        System.out.println(ldt2);

        String str = ldt2.format(DateTimeFormatter.ofPattern("dd-MM-yyyy ~~~ HH-mm-ss"));
        System.out.println(str);

    }
}


/*

        Find HOURS between:





                                        ChronoUnit - ENUM {To find the difference b/w 2 LocalDate or LocalDateTime)
                            .between(IncludeDate, ExcludeDate)
                            ChronoUnit.DAYS     .between(startDate,EndDate);
                            ChronoUnit.MONTHS   .between(startDate,EndDate);
                            ChronoUnit.YEARS    .between(startDate,EndDate);
                            ChronoUnit.HOURS    .between(startDate,EndDate);
                            ChronoUnit.MINUTES  .between(startDate,EndDate);

                            ✔ Gives TOTAL difference (single unit)
                            ✔ Start inclusive, end exclusive
                            ✔ Can return negative values

                            $$$$$$$ *** Works with LocalDate, LocalDateTime *** $$$$$$$

                            ✔ Full unit concept:
                            Months/Years count only if fully completed

                            ✔ Common Units:
                            DAYS, MONTHS, YEARS (MOST IMPORTANT)

                            ✔ Package:
                            java.time.temporal.ChronoUnit

LocalDate D1 = LocalDate.of(2025, 10, 10);
        LocalDate D2 = LocalDate.of(2025, 10, 10);

        System.out.println("1 -> "+ChronoUnit.DAYS.between(D1,D2)); // 0
        System.out.println("2 -> "+ChronoUnit.DAYS.between(D2,D1)); // 0

        LocalDate D3 = LocalDate.of(2025, 10, 9);
        LocalDate D4 = LocalDate.of(2025, 10, 10);
        System.out.println("3 -> "+ChronoUnit.DAYS.between(D3,D4)); // 1
        System.out.println("4 -> "+ChronoUnit.DAYS.between(D4,D3)); // -1

        LocalDate D5 = LocalDate.of(2025, 10, 1);
        LocalDate D6 = LocalDate.of(2025, 10, 10);
        System.out.println("5 -> "+ChronoUnit.DAYS.between(D5,D6)); // 9
        System.out.println("6 -> "+ChronoUnit.DAYS.between(D6,D5)); // -9
        System.out.println("7 -> "+ChronoUnit.MONTHS.between(D5,D6)); // 0

        LocalDate D7 = LocalDate.of(2025, 1, 25);
        LocalDate D8 = LocalDate.of(2025, 2, 1);
        System.out.println("8 -> "+ChronoUnit.MONTHS.between(D7,D8)); // 0

        LocalDate D9 = LocalDate.of(2025, 1, 25);
        LocalDate D10 = LocalDate.of(2025, 2, 25);
        System.out.println("9 -> "+ChronoUnit.MONTHS.between(D9,D10)); // 1  - Month passed

        LocalDate D11 = LocalDate.of(2025, 1, 20);
        LocalDate D12 = LocalDate.of(2025, 2, 28);
        System.out.println("10 -> "+ChronoUnit.MONTHS.between(D11,D12)); // 1  - Month passed

        LocalDate D13 = LocalDate.of(2003, 5, 30);
        LocalDate D14 = LocalDate.of(2026, 4, 15);
        System.out.println("DOB Y -> "+ChronoUnit.YEARS.between(D13,D14)); //  22 Years
        System.out.println("DOB M-> "+ChronoUnit.MONTHS.between(D13,D14)); //  274 Months
        System.out.println("DOB D-> "+ChronoUnit.DAYS.between(D13,D14));   //  8356 Days

        Period p = Period.between(D13, D14); //P 22Y 10M 16D
        System.out.println(p);







 */
