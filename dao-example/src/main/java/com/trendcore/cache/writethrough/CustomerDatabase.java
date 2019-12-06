package com.trendcore.cache.writethrough;

import com.trendcore.cache.regions.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerDatabase {

    private static CustomerDatabase customerDatabase = new CustomerDatabase();

    private List<Customer> customerRecords = new ArrayList<>();

    public static CustomerDatabase getInstance() {
        return customerDatabase;
    }

    public Customer find(String key) {
        if (key != null) {
            return customerRecords.stream().
                    filter(customer -> key.equals(customer.getId())).
                    findFirst().
                    orElse(null);
        }
        return null;
    }

    public void insert(Customer newValue) {
        customerRecords.add(newValue);
    }

    public void update(Customer newValue) {
        if (newValue != null) {
            customerRecords.
                    stream()
                    .filter(customer -> customer.getId().equals(newValue.getId()))
                    .forEach(customer -> {
                        customer.setFirstName(newValue.getFirstName());
                        customer.setFirstName(newValue.getLastName());
                        customer.setPhoneNo(newValue.getPhoneNo());
                        customer.setAddress(newValue.getAddress());
                    });
        }
    }
}
