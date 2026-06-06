package com.bank;

import com.bank.service.Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BankappApplicationTests {

	@Autowired
    Service bankService;

    @Test
    void demoMethod(){
        System.out.println("Start of Demo()");

        assertEquals(10, 5+5);
        assertNotNull(bankService);
        assertSame(null, bankService); //Will give issue

        System.out.println("End of Demo()");
    }
}
