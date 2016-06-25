package ru.urvanov.crossover.salesorder.dao;

import java.util.List;

import ru.urvanov.crossover.salesorder.domain.Customer;

public interface CustomerDao {
    
    Customer findById(Integer id);
    
    Customer findByCode(String code);
    
    List<Customer> findAll();
    
    void save(Customer customer);
    
    void delete(Integer id);
}
