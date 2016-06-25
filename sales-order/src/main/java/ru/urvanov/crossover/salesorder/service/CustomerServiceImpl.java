package ru.urvanov.crossover.salesorder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.urvanov.crossover.salesorder.dao.CustomerDao;
import ru.urvanov.crossover.salesorder.domain.Customer;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Override
    public Customer findById(Integer id) {
        return customerDao.findById(id);
    }
    
    @Override
    public Customer findByCode(String code) {
        return customerDao.findByCode(code);
    }

    @Override
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public void save(Customer customer) {
        customerDao.save(customer);
    }

    @Override
    public void delete(Integer id) {
        customerDao.delete(id);
    }

}
