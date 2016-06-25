package ru.urvanov.crossover.salesorder.service;

import java.util.List;

import ru.urvanov.crossover.salesorder.domain.Product;

public interface ProductService {

    Product findById(Integer id);

    Product findByCode(String code);
    
    List<Product> findAll();

    void save(Product product);

    void delete(Integer id);

    

}
