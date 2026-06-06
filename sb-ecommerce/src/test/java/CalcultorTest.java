import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    @Test
    void testAddPass() {
        int result = 2 + 3;
        assertEquals(2, result); //Fail: "AssertionFailedError"
        System.out.println("ABC"); //This line will not execute
    }
    @Test
    void testAddFail() {
        int result = 2 + 3;
        assertEquals(5, result); //Pass
        System.out.println("DEF"); //This will execute - as each method execute
                                   //                             Independently
    }

    @Test
    void testTrue() {

        int age = 20;

        assertTrue(age > 18);
    }

    @Test
    void testFalse() {

        boolean isEmpty = false;

        assertFalse(isEmpty);
        System.out.println("End of testFalse Method");
    }

    @Test
    void testNotNull() {

        String name = "Affan";

        assertNotNull(null);
        System.out.println("***************Testing End***************");
    }
}
