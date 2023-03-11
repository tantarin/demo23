package com.example.demo.controller;


import com.example.demo.entity.Customer;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {

    Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private CustomerService customerService;

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerController(CustomerService customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        List<Customer> customers = customerService.getAll();
        model.addAttribute("customers", customers);
        return "customers";
    }

    @GetMapping(path={"/add", "/edit/{id}"})
    public String addCustomer(Model model, @PathVariable Optional<Long> id) {
        if (id.isPresent()) {
            model.addAttribute("customer", customerRepository.findById(id.get()).get());
        }
        else {
            model.addAttribute("customer", new Customer());

        }
        return "add-customer";
    }

    @PostMapping(path = "/createOrUpdateCustomer")
    public String createOrUpdateCustomer(Customer customer) {
        if (customer.getId() == null) {
            customerService.save(customer);
        }
        else {
            customerService.update(customer);
        }
        return "redirect:/all";
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteCustomerById(@PathVariable("id") Long id) {
        logger.info("deleting the customer, Id:" + id);
        customerService.delete(id);
        return "redirect:/all";
    }

}
