package ru.urvanov.crossover.salesorder.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import ru.urvanov.crossover.salesorder.domain.Product;
import ru.urvanov.crossover.salesorder.test.SpringTestBase;

public class ProductDaoImplTest extends SpringTestBase {
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private DataSource dataSource;
    

    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(new FileInputStream(
                        "src/test/java/ru/urvanov/crossover/salesorder/dao/ProductDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection()) {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testFindById1() {
        Product product = productDao.findById(101);
        assertNotNull(product);
        assertEquals("01", product.getCode());
        assertEquals("Product 01", product.getDescription());
        assertEquals(0, product.getPrice().compareTo(new BigDecimal("45.00")));
        assertEquals(Integer.valueOf(3), product.getQuantity());
    }
    
    @Test
    public void testFindByCode1() {
        Product product = productDao.findByCode("01");
        assertNotNull(product);
        assertEquals("01", product.getCode());
        assertEquals("Product 01", product.getDescription());
        assertEquals(0, product.getPrice().compareTo(new BigDecimal("45.00")));
        assertEquals(Integer.valueOf(3), product.getQuantity());
    }
    
    @Test
    public void testFindAll() {
        List<Product> products = productDao.findAll();
        assertNotNull(products);
        assertEquals(3, products.size());
    }
    
    @Test
    public void testSaveNew1() {
        Product product = new Product();
        product.setCode("33333");
        product.setDescription("33333333333");
        product.setPrice(new BigDecimal("999.99"));
        product.setQuantity(999);
        productDao.save(product);
        assertNotNull(product.getId());
        
        Product savedProduct = productDao.findById(product.getId());
        assertNotNull(savedProduct);
        assertEquals(product.getCode(), savedProduct.getCode());
        assertEquals(product.getDescription(), savedProduct.getDescription());
        assertEquals(0, savedProduct.getPrice().compareTo(product.getPrice()));
        assertEquals(product.getQuantity(), savedProduct.getQuantity());
    }
    
    @Test
    public void testSaveOld1() {
        Product product = productDao.findById(101);
        product.setCode("33333");
        product.setDescription("33333333333");
        product.setPrice(new BigDecimal("999.99"));
        product.setQuantity(999);
        productDao.save(product);
        assertNotNull(product.getId());
        
        Product savedProduct = productDao.findById(101);
        assertNotNull(savedProduct);
        assertEquals(product.getCode(), savedProduct.getCode());
        assertEquals(product.getDescription(), savedProduct.getDescription());
        assertEquals(0, savedProduct.getPrice().compareTo(product.getPrice()));
        assertEquals(product.getQuantity(), savedProduct.getQuantity());
    }
    
    @Test
    public void testDelete1() {
        productDao.delete(101);
        assertEquals(2, productDao.findAll().size());
    }
}
