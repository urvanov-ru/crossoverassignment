package ru.urvanov.crossover.salesorder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.urvanov.crossover.salesorder.dao.ProductDao;
import ru.urvanov.crossover.salesorder.domain.Product;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public Product findById(Integer id) {
        return productDao.findById(id);
    }
    
    @Override
    public Product findByCode(String code) {
        return productDao.findByCode(code);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public void save(Product product) {
        productDao.save(product);
    }

    @Override
    public void delete(Integer id) {
        productDao.delete(id);
    }

}
