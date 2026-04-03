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
        return "Book{name='" + name + "', category='" + category + "'}";
    }
}

public class StreamNotes {
    public static void main(String[] args) {
        //Chaining and pipelining of Optional methods//

        Optional<String> name = Optional.of("java");
        name.filter(a->a.startsWith("j"))
                .map(a->a.toUpperCase())
                .ifPresent(System.out::println);


        //
//        Optional<String> fullOptional = Optional.of("Syed will be a Java DEVELOPER Inshallah");
//        Optional<String> nullOptional1 = Optional.ofNullable(null);
//
//        Optional<String> o1 = fullOptional.map(a->a.toUpperCase());
//        Optional<String> o2 = nullOptional1.map(a->a.toUpperCase());
//        System.out.println(o1); //Optional[SYED WILL BE A JAVA DEVELOPER INSHALLAH]
//        System.out.println(o2); //Optional.empty
//




//        // .ifPresent(Consumer<>) //Consumer logic only works when something not-null/not empty is there in Optional
//        fullOptional.ifPresent((a)-> System.out.println("######: " + a)); //Will print
//        nullOptional1.ifPresent((a)-> System.out.println("XXXXXXXXXXXXXXXXXXXXX")); //Will Not print



//        /* .orElseThrow(Supplier<retunr Excpetion>) */
//        System.out.println(  fullOptional.orElseThrow( ()->  new RuntimeException()) ) ; //Will Work smoothly and return String
//        System.out.println(  nullOptional1.orElseThrow( ()->  new RuntimeException())) ; //Will raise RuntimeException






    }
}