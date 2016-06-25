package ru.urvanov.crossover.salesorder.dao;

import java.util.List;

import ru.urvanov.crossover.salesorder.domain.Product;

public interface ProductDao {

    Product findById(Integer id);
    
    Product findByCode(String code);
    
    List<Product> findAll();
    
    void save(Product product);
    
    void delete(Integer id);
}
