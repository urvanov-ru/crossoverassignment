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

import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.test.SpringTestBase;

public class CustomerDaoImplTest extends SpringTestBase {

    @Autowired
    private CustomerDao customerDao;

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
                        "src/test/java/ru/urvanov/crossover/salesorder/dao/CustomerDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection()) {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testFindById1() {
        Customer customer = customerDao.findById(101);
        assertNotNull(customer);
        assertEquals(Integer.valueOf(101), customer.getId());
        assertEquals("01", customer.getCode());
        assertEquals("Customer 1", customer.getName());
        assertEquals("+7000000000", customer.getPhone1());
        assertEquals(0,
                new BigDecimal("100.00").compareTo(customer.getCurrentCredit()));
    }
    
    @Test
    public void testFindByCode1() {
        Customer customer = customerDao.findByCode("01");
        assertNotNull(customer);
        assertEquals(Integer.valueOf(101), customer.getId());
        assertEquals("01", customer.getCode());
        assertEquals("Customer 1", customer.getName());
        assertEquals("+7000000000", customer.getPhone1());
        assertEquals(0,
                new BigDecimal("100.00").compareTo(customer.getCurrentCredit()));
    }

    @Test
    public void testFindAll1() {
        List<Customer> customers = customerDao.findAll();
        assertNotNull(customers);
        assertEquals(3, customers.size());
    }

    @Test
    public void testSaveNew1() {
        Customer customer = new Customer();
        customer.setCode("3333");
        customer.setName("Customer 3333");
        customer.setPhone1("0000000");
        customer.setPhone2("1111111");
        customer.setAddress("address some");
        customer.setCurrentCredit(new BigDecimal("100.00"));
        customer.setCreditLimit(new BigDecimal("200.00"));
        customerDao.save(customer);
        assertNotNull(customer);
        Customer savedCustomer = customerDao.findById(customer.getId());
        assertNotNull(savedCustomer);
        assertEquals(customer.getCode(), savedCustomer.getCode());
        assertEquals(customer.getName(), savedCustomer.getName());
        assertEquals(customer.getPhone1(), savedCustomer.getPhone1());
        assertEquals(customer.getPhone2(), savedCustomer.getPhone2());
        assertEquals(customer.getAddress(), savedCustomer.getAddress());
        assertEquals(customer.getCurrentCredit().compareTo(savedCustomer.getCurrentCredit()), 0);
        assertEquals(customer.getCreditLimit().compareTo(savedCustomer.getCreditLimit()), 0);
    }


    @Test
    public void testSave1() {
        Customer customer = customerDao.findById(101);
        customer.setCode("3333");
        customer.setName("Customer 3333");
        customer.setPhone1("0000000");
        customer.setPhone2("1111111");
        customer.setAddress("Some address");
        customer.setCurrentCredit(new BigDecimal("100.00"));
        customer.setCreditLimit(new BigDecimal("100.00"));
        customerDao.save(customer);
        assertNotNull(customer);
        Customer savedCustomer = customerDao.findById(customer.getId());
        assertNotNull(savedCustomer);
        assertEquals(customer.getCode(), savedCustomer.getCode());
        assertEquals(customer.getName(), savedCustomer.getName());
        assertEquals(customer.getPhone1(), savedCustomer.getPhone1());
        assertEquals(customer.getPhone2(), savedCustomer.getPhone2());
        assertEquals(customer.getAddress(), savedCustomer.getAddress());
        assertEquals(customer.getCurrentCredit().compareTo(savedCustomer.getCurrentCredit()), 0);
        assertEquals(customer.getCreditLimit().compareTo(savedCustomer.getCreditLimit()), 0);
    }
    
    @Test
    public void testDelete1() {
        customerDao.delete(101);
        assertEquals(2, customerDao.findAll().size());
    }

}
