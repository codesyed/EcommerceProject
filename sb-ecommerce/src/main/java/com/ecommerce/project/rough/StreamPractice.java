package com.ecommerce.project.rough;


import java.util.*;
import java.util.stream.Stream;

class Employee {
    Integer id;
    String name;
    Double salary;
    String department;

    public Employee(Integer id, String name, Double salary, String department) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.department = department;


    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", department='" + department + '\'' +
                '}';
    }
}



public class StreamPractice {

    public static void main(String[] args) {
        Map<String, String>map=new HashMap<>();
        map.put("one", "1");
        map.put("two", "2");
        map.put("three","3");
        map.put("four", "4");

        Collection<String> coll = map.values();
        System.out.println(coll);

        Set<String> keySet = map.keySet();
        System.out.println(keySet);

        Set<Map.Entry<String, String>> entrySet=map.entrySet();
        for(Map.Entry<String, String> entry: entrySet){
            System.out.println("key-> "+entry.getKey()+": value-> "+entry.getValue());
        }
    }
}
/*
Point 0:- .stream()
Point 1:- Stream.iterate(100, (num)->num+1).limit(3).forEach(System.out::println); [100,101,102]
Point 2:- Stream.iterate(startValue, Consumer).limit(sizeOfFinalStream).TERMINAL_OPERATION
Point 3:- Stream.iterate(2, val->val+2).map(val->val*val).limit(5).toList(); ---> square of first 5 evens

Point 4:- .sorted((a,b)->Double.compare(a,b))  ---> .sortED return Stream again.
Point 5:- .sorted(Comparator.comparing(Employee::getSalary))

           [Comparator.comparing(emp.getId()) -> return Comparator Object sorting on 'Id']
           [ComparatorObject . reverse() -> reverse the logic of sorting]
Point 6:- .sorted((e1,e2)->Double.compare(e2.getSalary(), e1.getSalary()).forEach(System.out::println)
Point 7:- .sorted( Comparator.comparing(emp->emp.getSalary()) . reversed() ).toList();
Point 8:- .sorted( Comparator.comparing(Employee::getSalary()) .reversed() ).toList();

Point 9:- .findFirst() ---> find SINGLE value/NULL therefore <Optional>
Point 10:- .max(COMPARATOR-OBJECT)/.min(COMPARATOR-OBJECT) ---> find SINGLE value/NULL therefore <OPTIONAL>
Point 11:-  Optional<String> maxStr = strList.stream() .max(Comparator.comparing(String::length));

Point 11:- .filter(emp-> emp.getSalary()>1000)

Point 12:- .reduce(startValAcc, (acc, num)-> acc+num);
Point 13:- .reduce(0, Integer::sum);
Point 14:- $ Use ternary operator inside reduce For if else checking $

Point 15:- .stream().map(str->str.toUpperCase()).toList()
Point 16:- .stream.map(String::toUpperCase())

1- [filter() on Stream<Employee> returns Stream<Employee> because it only filters existing Employee objects
        -> and does not change their type.]

2- [map() can return ANY type depending on what you map to.
        -> Employee -> String | Employee -> Integer | Employee -> Double | Employee -> Department etc.]

3- [reduce() always operates on the CURRENT stream type:
        -> If reduce() is applied directly on Stream<Employee> (without any map() in between):
    • Identity (Seed)      -> Employee
    • Accumulator (acc)    -> Employee
    • Current Value (emp)  -> Employee
    • Return Type          -> Employee

    It combines multiple Employee objects into one final Employee object.]

[ If you want reduce() to operate on salary instead of Employee objects, first convert the stream:

  Stream<Employee>
        ↓
 .map(Employee::getSalary) ------> .map( <Consumer> )
        ↓
 Stream<Integer>

 Now reduce() will work on Integer values and return an Integer result ]

Point 19:- salarySum = list.stream().map(emp->emp.getSalary()).reduce(0, (acc, sal)->acc+sal);

[.mapToInt() convert To IntStream-Primitive on which .sum() is defined]
Point 20:- salarySum = list.stream().mapToInt(emp->emp.getSalary()).sum();
Point 21:- avgLength = list.stream().mapToInt(str->str.lenght()).average();

[IntStream(of primitive) gives methods like .sum(), .average()
    -> but it can't be converted to List/Set or collectin Directly]
[Stream(of objects) gives methods like .toList(), collect(Collectors.toSet)
    -> but it doesn't provide .sum, .average()]

[IntStream/DoubleStream/DoubleStream {Primitive} --> To --> Stream<objects> use .boxed()
and Stream<object> to primitives use .mapToInt()/.mapToDouble()/.mapToDouble()]

Point 22:- Find avg salary of employees:
           OptionalDouble avgSal = list.stream().mapToDouble(emp->emp.getSalary()).average();
           double avg = avgSal.getAsDouble()

For General Grouping:                  [.collect(Collectors.groupingBy(function)]
For Numeric counting:        [.collect(Collectors.groupingBy(function, Collectors.counting()))]

Point 17:- Map<String, List<Employee>> deptWise = list.stream().collect(Collectors.groupingBy(emp->emp.getDepartment()));
Point 18:- Map<String, Lists<Employee>> deptWise = list.stream().collect(Collectors.groupingBy(Employee::getDepartment));
Point 19: Map<String, Long> deptWiseEmpCount = list.stream().collect(Collectors.gropingBy(Employee::getDepartment) ,Collectors.counting());

                            [List<Employee> to Map<EmpId, EmpName>]
                            [Collectors.toMap(Employee::getId, Employee.getName)]
                            [Collectors.toMap( keyMapper, valueMapper )]

Point 20: Map<Integer, String> idWiseEmpName = list.stream().collect( Collectors.toMap( emp->emp.getId(), emp->getName() );
Point 21: Map<Integer, String> idWiseEmpName = list.stream().collect( Collectors.toMap( Employee::getId, Employee::getName ));

                            [List<Employee> to Map<EmpName, thatEmpObject>]
Point 22: Map<Integer, Employee> nameWiseEmpObject = list.stream().collect( Collectors.toMap( Employee::getName, emp->emp));
Point 23: Map<Integer, Employee> nameWiseEmpObject = list.stream().collect( Collectors.toMap( Employee::getName,  Function.identity());
                            { Function.identity() === same as === (emp) -> emp }
                         {Returns: a function that always returns its input argument}

Point 24:  [FollowUp Interview: what is Name of Employees is duplicate? : IllegalStateException: Duplicate key Found]
                                                  [Solution]

Map<String, Employee> nameWiseEmpObj = list.stream()
                        .collect(
                                Collectors.toMap(
                                            Employee::getName ,
                                            Function.identity() ,
                                            (oldVal, newVal)->oldVal) //Take oldOne
                          );




Point 25:                            [Finding Duplicates using Stream]
Point :Set<Integer> seen = new HashSet<>();
         List<Integer> allDuplicates =  numbers.stream().filter((num)-> {
                                            boolean isAdded=seen.add(num);
                                            return !isAdded;
                                        }).toList();



[$ Remove Duplicates === .distinct() $]
Point 22:- .distinct()
Point 23:- .skip(5) ---> skip first 5 elements from stream
Point 24:- .anyMatch(<Predicate>)   => IMP
Point 25:- .allMatch(<Predicate>)   => IMP
Point 26:- .noneMatch(<Predicate>)  => IMP

[Q: Check whether all Employees belong to a particular Department using allMatch()]
boolean ans = list.stream() .allMatch(emp->emp.getDepartment().equalsIgnoreCase("ADM"));

[Q: Check whether any Employee has salary greater than a given amount using anyMatch()]
boolean ans2 = list.stream() .anyMatch(emp->emp.getSalary() > 505D);

[Q: Check whether no Employee belongs to a given Department using noneMatch()]
boolean ans3 = list.stream() .noneMatch(emp->emp.getDepartment().equalsIgnoreCase("Orcall"));


Terminal Operations:
Point :- .count() ---> Return size of stream

Stream Pipelining : Chaining multiple operations on a stream
                    Flow: Source → Lazy ops → Terminal op


                                   ==================================
                                         [ map() VS flatMap() ]
                                   ==================================
        List<Employee>list=new ArrayList<>();
                list.add(new Employee(101, "A", 1D, "CIS", new ArrayList<>(Arrays.asList("555", "786", "999"))));
                list.add(new Employee(102, "B", 2D, "ADM", new ArrayList<>(Arrays.asList("555", "781", "919"))));
                list.add(new Employee(103, "C", 5D, "ADM", new ArrayList<>(Arrays.asList("555", "101", "990"))));


        List<List<String>> nestedPhoneNumbers =
                list.stream()
                        .map(emp-> emp.getPhoneNumberList()) //Here result : Stream<List<String>>
                        .toList(); //Here result : List< List<String> >

                System.out.println("nestedPhoneNumbers: "+nestedPhoneNumbers);

        List<String> flatPhoneNumbers = list.stream()
                .flatMap(emp-> emp.getPhoneNumberList().stream()) //Here result : Stream<String>
                .toList(); //Here result : List<String>

                System.out.println("flatPhoneNumbers: "+flatPhoneNumbers);

           -> map : transform 'single' element to other 'single' element
               + E.G.  map(Emp -> Emp.getSalary())  from Stream<Employee> to Stream<Double>/Stream<'Salary'>
               + Only One to One Mapping
               + Used when One to One Mapping exist in Entity/Class and its related filed with relationship to it.

           -> flatMap :
              Suppose 1 Employee object has List<String>phoneNumberList
              + If we want our final List of Numbers as Nested (means no flattening is needed)
              we can use simple .map(Emp->Emp.getPhoneNumberList) //This will return Stream<List<String>
                                .toList()                       //This will finally return List<List<String>>

             $ If we want our final phoneNumbers of ALL EMPLOYEES inside Flattened - 1D List
             Use:    .flatMap(Emp->Emp.getPhoneNumberList . stream()) //This will return Stream<String>
                     .toList()                                       //This will finally return List<String> 1D


            // Flatten a List<List<String>> into a single List<String> using flatMap().
            ==> nestedList.stream().flatMap( row -> row.stream() ).toList();

                                   ==================================
                                                CODES
                                   ==================================


CODE 01:
        //Q: Find the Employee with MINIMUM SALARY using .reduce()

        Employee seedEmp=new Employee(null, null, Double.MAX_VALUE);
        Employee empWithMinSalary =
                        list.stream()
                        .reduce(seedEmp, (accEmp, currEmp)->
                                {
                                    if(accEmp.salary<=currEmp.salary)return accEmp;
                                    return currEmp;
                                }
                        );

        System.out.println("empWithMinSalary.toString()--> "+empWithMinSalary.toString());
                        //->Employee{id=101, name='Fajr', salary=1.0}

        OptionalDouble minSalAmount1 = list.stream()
                .mapToDouble(employee -> employee.salary)
                .min();
        System.out.println("minSalAmount1--> "+minSalAmount1); //---> OptionalDouble[1.0]

        Optional<Double> minSalAmount2 = list.stream()
                .map(employee -> employee.salary)
                        .min((sal1, sal2)-> Double.compare(sal1,sal2));

        System.out.println("minSalAmount2--> "+minSalAmount2); //---> Optional[1.0]


Code 02:
        //Q: find the longest size string of list using reduce()
        String ans = strList.stream()
                    .reduce("", (accStr, currStr)->{
                        if(accStr.length()>=currStr.length())return accStr;
                        return currStr;
                    });

        System.out.println(ans);


Code 03:
        //Q: Find the highest salaried Employee from a List using Stream API.

        //--> Approach 01: .sorted() and .findFirst()
        Optional<Employee> op1= list.stream()
        .sorted(Comparator.comparing(Employee::getSalary).reversed())
        .findFirst();

        //--> Approach 02: .max(ComparatorObject)
        Optional<Employee> op2 = list.stream()
        .max(Comparator.comparing(Employee::getSalary));

        //--> Approach 03: .reduce(dummyEmpSeedWithMINSal, (accEmp, currEmp)->{})
        Employee dummyEmp = new Employee(null, null, Double.MIN_VALUE, null);

        Employee employeeWithMaxSalary = list.stream()
        .reduce(dummyEmp, (accEmp, currEmp)->{
            if(accEmp.getSalary()>=currEmp.getSalary())return accEmp;
            return currEmp;
        });

        System.out.println("op1: "+op1);
        System.out.println("op2: "+op2);
        System.out.println("employeeWithMaxSalary: "+employeeWithMaxSalary);

Code 04:
        //Q: Find the Top 3 highest salaried Employee from an Employee List using Stream API.
        List<Employee> top3SalariedEmps = list.stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                        .limit(3)
                                .toList();


        //Q: Find the Top 3 highest salaries from an Employee List using Stream API.
        List<Double> top3Salaries = list.stream()
                        .map(Employee::getSalary)
                                .sorted((s1,s2)-> Double.compare(s2,s1))
                                .limit(3)
                                        .toList();

        System.out.println("top3SalariedEmps:-  "+ top3SalariedEmps);
        System.out.println("top3Salaries:- "+ top3Salaries);

CODE 05:
        //Q: Find one duplicate element from a List using Stream API.
        Set<Integer>set = new HashSet<>();

        Optional<Integer>duplicate = numbers.stream()
                                            .filter((num)-> {
                                                boolean isAdded = set.add(num);
                                                return !isAdded;
                                                //set.add(num) return true if element added otherwise false(means already exist in set)
                                            })
                                            .findFirst();
        System.out.println("duplicate:- "+duplicate);

        //Q: Find all duplicate elements from a List using Stream API
        Set<Integer> seenBefore = new HashSet<>();
        List<Integer> allDuplicates = numbers.stream()
                .filter(num -> {
                    boolean isAdded = seenBefore.add(num);
                    return !isAdded;
                })
                .toList();

        System.out.println("allDuplicates:- "+allDuplicates);

Code 06:
        [ ** List to Map Conversion using Streams **  Collectors.toMap(..key..,..val..) ** ]

        //Q:    Convert List<Employee> to Map<EmpId, thatEmployeeObject>
        Map<Integer, Employee> idWiseEmployees =
                                list.stream()
                                .collect(
                                    Collectors.toMap(emp->emp.getId(), emp->emp)
                                );

        //Q:    Convert List<Employee> to Map<EmpId, thatEmpName>
        Map<Integer, String> idWiseEmpName =
                list.stream().collect(Collectors.toMap(Employee::getId, emp -> emp.getName()));

        Map<Integer, String> idWiseEmpName2 =
                list.stream().collect(Collectors.toMap(Employee::getId, Employee::getName));

        System.out.println("idWiseEmployees -> "+idWiseEmployees);
        System.out.println("\nidWiseEmpName -> "+idWiseEmpName);
        System.out.println("\nidWiseEmpName2 -> "+idWiseEmpName2);
 */