package com.ecommerce.project;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Optional;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
        import static org.junit.jupiter.api.Assertions.*;
         /* Example Code For Testing Service Class */
/* Repository */
interface ProductRepository {
    Optional<Product> findById(Long id);
}
/* Entity */
class Product {
    Long id;  String name;
    Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
/* Service */
class ProductService {
    ProductRepository productRepository;
    ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    Product getProduct(Long id) {
        return productRepository.findById(id).get();
    }
}

/* Test Class */
@ExtendWith(MockitoExtension.class) //To use @Mock+@MockBean+@InjectMock
public class ProductServiceTest {

    /* STEP 1:vCreate fake/mock repository */
    @Mock
    ProductRepository productRepository;

    /* STEP 2: Inject fake repository into service */
    @InjectMocks //Step 01's mock object will be injected here.....
    ProductService productService;

    @Test
    void testGetProduct() {
        /* STEP 3: Dummy/Fake product */
        Product product = new Product(1L, "Laptop");

        /* STEP 4: Define fake behavior */
        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        /* STEP 5: Call service method - Which we are testing actually */
        Product result = productService.getProduct(1L);

        /* STEP 6: Assertions -> To check expected vs actual output */
        assertNotNull(result);
        assertEquals("Laptop", result.name);

        /* STEP 7: Verify interaction Will PASS if .findById() was executed Actually Otherwise FAIL */
        verify(productRepository).findById(1L);
    }
}