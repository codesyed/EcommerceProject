package com.bank.service;
import com.bank.customer.Customer;
import com.bank.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceImpl implements Service{

    private final CustomerRepository repository;

    public ServiceImpl(CustomerRepository repository) {
        this.repository = repository;
        System.out.println("****** Default constructor of ServiceImpl *******");
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return repository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return  repository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item with this id: "+id+"Dosen't Exists!"));
    }

    @Override
    public Customer updateCustomer(Customer newCustomer, Long oldId) {

        Optional<Customer> op = repository.findById(oldId);

        return op.map(oldCust -> {
//           oldCust.setId(newCustomer.getId()); --> Not allowed
           oldCust.setEmail(newCustomer.getEmail());
           oldCust.setFirstName(newCustomer.getFirstName());
           oldCust.setLastName(newCustomer.getLastName());
           oldCust.setPhoneNumber(newCustomer.getPhoneNumber());
           repository.save(oldCust);
           return oldCust;
        }).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with this id: "+oldId+"Dosen't Exists!"));
    }

    @Override
    public void deleteCustomer(Long id) {
        if(repository.existsById(id))
            repository.deleteById(id);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer with this id: "+id+"Dosen't Exists!");
    }
}
