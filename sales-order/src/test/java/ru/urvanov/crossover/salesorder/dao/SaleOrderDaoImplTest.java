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

import ru.urvanov.crossover.salesorder.domain.OrderLine;
import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.domain.SaleOrderListItem;
import ru.urvanov.crossover.salesorder.test.SpringTestBase;

public class SaleOrderDaoImplTest extends SpringTestBase {

    @Autowired
    private SaleOrderDao saleOrderDao;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ProductDao productDao;

    @Before
    public void before() throws Exception {

        // initialize your database connection here
        IDatabaseConnection databaseConnection = null;
        // ...

        // initialize your dataset here
        IDataSet dataSet = new FlatXmlDataSetBuilder()
                .build(new FileInputStream(
                        "src/test/java/ru/urvanov/crossover/salesorder/dao/SaleOrderDaoImplTest.xml"));

        try (Connection connection = dataSource.getConnection()) {
            databaseConnection = new DatabaseConnection(connection);
            DatabaseOperation.CLEAN_INSERT.execute(databaseConnection, dataSet);
        }
    }

    @Test
    public void testFindById1() {
        SaleOrder saleOrder = saleOrderDao.findById(201);
        assertNotNull(saleOrder);
        assertNotNull(saleOrder.getCustomer());
        assertEquals(Integer.valueOf(201), saleOrder.getId());
        assertEquals(Integer.valueOf(101), saleOrder.getCustomer().getId());
    }
    
    @Test
    public void testFindFullByNumber1() {
        SaleOrder saleOrder = saleOrderDao.findFullByNumber("01");
        assertNotNull(saleOrder);
        assertNotNull(saleOrder.getCustomer());
        assertEquals(Integer.valueOf(201), saleOrder.getId());
        assertEquals(Integer.valueOf(101), saleOrder.getCustomer().getId());
    }

    @Test
    public void testFindFullById() {
        SaleOrder saleOrder = saleOrderDao.findFullById(201);
        assertNotNull(saleOrder);
        assertNotNull(saleOrder.getOrderLines());
        assertEquals(2, saleOrder.getOrderLines().size());
    }

    @Test
    public void testFindAll() {
        List<SaleOrder> saleOrders = saleOrderDao.findAll();
        assertNotNull(saleOrders);
        assertEquals(3, saleOrders.size());
    }

    @Test
    public void testSaveNew1() {
        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomer(customerDao.findById(101));
        OrderLine orderLine = new OrderLine();
        orderLine.setSaleOrder(saleOrder);
        orderLine.setProduct(productDao.findById(401));
        orderLine.setPrice(new BigDecimal("444.00"));
        orderLine.setQuantity(45);
        saleOrder.getOrderLines().add(orderLine);
        saleOrderDao.save(saleOrder);

        assertNotNull(saleOrder.getId());

        SaleOrder savedSaleOrder = saleOrderDao.findFullById(saleOrder.getId());
        assertEquals(saleOrder.getCustomer(), savedSaleOrder.getCustomer());
        OrderLine savedOrderLine = savedSaleOrder.getOrderLines().iterator()
                .next();
        assertEquals(orderLine.getProduct(), savedOrderLine.getProduct());
        assertEquals(0,
                savedOrderLine.getPrice().compareTo(orderLine.getPrice()));
        assertEquals(orderLine.getQuantity(), savedOrderLine.getQuantity());
    }

    @Test
    public void testDelete() {
        saleOrderDao.delete(201);
        assertEquals(2, saleOrderDao.findAll().size());
    }
    
    @Test
    public void testList() {
        List<SaleOrderListItem> list = saleOrderDao.list();
        assertNotNull(list);
        assertEquals(3, list.size());
        assertEquals(0, list.get(0).getTotalPrice().compareTo(new BigDecimal("86.06")));
    }

}
