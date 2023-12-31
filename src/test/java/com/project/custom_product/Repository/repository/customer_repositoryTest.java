package com.project.custom_product.Repository.repository;

import com.project.custom_product.Respository.customer_repository;
import com.project.custom_product.entities.Customer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest(  properties = {
    "spring.jpa.properties.javax.persistence.validation.mode=none"
})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class customer_repositoryTest {

    @Autowired    
    private customer_repository repos;

    Customer customer1 ;
    Customer customer2 ;

    @BeforeEach

    void setup(){

        customer1 = Customer.builder()
            .first_name("Ryan")
            .last_name("Henry")
            //  .password("")
            .address("Hans Holmboes gate 9 ")
            .telefon_number("00000000")
            .build();
            
        customer2 = Customer.builder()
            .first_name("Ahmed")
            .last_name("Ali")
            //  .password("")
            .address("Sognsveien 102 T ")
            .telefon_number("00000001")
            .build();

        repos.save(customer1);
        repos.save(customer2);
   



    }

    @AfterEach

     void teardown(){

        repos.deleteAll();
     }

     

    @Test
    public void save_All_customers(){

        Customer saved = repos.save(customer1);
        Assertions.assertThat(saved).isNotNull();
        Assertions.assertThat(saved.getId()).isGreaterThan(0);
    }

    @Test

    public void get_All_customers(){

   

         Iterable<Customer> customers = repos.findAll();

         Assertions.assertThat(customers).isNotNull();

         Assertions.assertThat(customers).size().isEqualTo(2);

    }


    @Test

    public void check_findCustomerById(){

        Customer Iscustomer1_exists = repos.findById(customer1.getId()).get();
        
        Assertions.assertThat(Iscustomer1_exists).isNotNull();
    }


    @Test 

    public void check_findCustomerByTelefon_number(){

         Customer Iscustomer2_exists = repos.findByTelefon_Number(customer2.getTelefon_number()).get();

         Assertions.assertThat(Iscustomer2_exists).isNotNull();

    }

    @Test 

     public void check_updateCustomer(){

        Customer customer = repos.findById(customer1.getId()).get();
        customer.setFirst_name("Jack");
        customer.setLast_name("Henderson");
        customer.setAddress("st. ullevaal 10");
        customer.setTelefon_number("111111111");

        Customer updated = repos.save(customer);

        Assertions.assertThat(updated.getFirst_name()).isNotNull();
        Assertions.assertThat(updated.getLast_name()).isNotNull();
        Assertions.assertThat(updated.getAddress()).isNotNull();
        Assertions.assertThat(updated.getTelefon_number()).isNotNull();


     }

     @Test
      public void check_deleteCustomer(){

        Customer customer = repos.findById(customer1.getId()).get();

        Assertions.assertThat(customer).isNotNull();

        repos.delete(customer);

        Optional<Customer>  check_deleted_customer = repos.findById(customer1.getId());

        Assertions.assertThat(check_deleted_customer).isEmpty();
      }

}