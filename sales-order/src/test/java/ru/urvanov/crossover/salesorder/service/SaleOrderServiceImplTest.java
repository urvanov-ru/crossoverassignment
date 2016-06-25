package ru.urvanov.crossover.salesorder.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;

import org.junit.Test;
import org.mockito.Mockito;

import ru.urvanov.crossover.salesorder.dao.CustomerDao;
import ru.urvanov.crossover.salesorder.dao.ProductDao;
import ru.urvanov.crossover.salesorder.dao.SaleOrderDao;
import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.domain.OrderLine;
import ru.urvanov.crossover.salesorder.domain.Product;
import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.exception.CustomerCreditLimitReachedException;
import ru.urvanov.crossover.salesorder.exception.OutOfProductException;

public class SaleOrderServiceImplTest {

    SaleOrderServiceImpl saleOrderService = new SaleOrderServiceImpl();

    @Test
    public void testSave1() {
        final int CUSTOMER_ID = 10;
        final int PRODUCT_ID = 333;

        CustomerDao customerDaoMock = Mockito.mock(CustomerDao.class);
        saleOrderService.setCustomerDao(customerDaoMock);

        ProductDao productDaoMock = Mockito.mock(ProductDao.class);
        saleOrderService.setProductDao(productDaoMock);
        saleOrderService.setSaleOrderDao(Mockito.mock(SaleOrderDao.class));

        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setCreditLimit(new BigDecimal("100"));
        customer.setCurrentCredit(new BigDecimal("50"));
        Mockito.when(customerDaoMock.findById(CUSTOMER_ID))
                .thenReturn(customer);

        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setQuantity(3);
        Mockito.when(productDaoMock.findById(PRODUCT_ID)).thenReturn(product);

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomer(customer);
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(2);
        orderLine.setPrice(new BigDecimal("10"));
        saleOrder.getOrderLines().add(orderLine);

        saleOrderService.save(saleOrder);

        assertEquals(Integer.valueOf(1), product.getQuantity());
        assertEquals(0,
                new BigDecimal("70").compareTo(customer.getCurrentCredit()));

    }

    @Test
    public void testSave2() {
        final int CUSTOMER1_ID = 10;
        final int CUSTOMER2_ID = 20;
        final int PRODUCT_ID = 333;
        final int SALE_ORDER_ID = 35454;

        CustomerDao customerDaoMock = Mockito.mock(CustomerDao.class);
        saleOrderService.setCustomerDao(customerDaoMock);

        ProductDao productDaoMock = Mockito.mock(ProductDao.class);
        saleOrderService.setProductDao(productDaoMock);

        SaleOrderDao saleOrderDaoMock = Mockito.mock(SaleOrderDao.class);
        saleOrderService.setSaleOrderDao(saleOrderDaoMock);

        Customer customer1 = new Customer();
        customer1.setId(CUSTOMER1_ID);
        customer1.setCreditLimit(new BigDecimal("100"));
        customer1.setCurrentCredit(new BigDecimal("50"));
        Mockito.when(customerDaoMock.findById(CUSTOMER1_ID)).thenReturn(
                customer1);

        Customer customer2 = new Customer();
        customer2.setId(CUSTOMER2_ID);
        customer2.setCreditLimit(new BigDecimal("99"));
        customer2.setCurrentCredit(new BigDecimal("99"));
        Mockito.when(customerDaoMock.findById(CUSTOMER2_ID)).thenReturn(
                customer2);

        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setQuantity(3);
        Mockito.when(productDaoMock.findById(PRODUCT_ID)).thenReturn(product);

        SaleOrder saleOrderOld = new SaleOrder();
        saleOrderOld.setId(SALE_ORDER_ID);
        saleOrderOld.setCustomer(customer2);
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(2);
        orderLine.setPrice(new BigDecimal("10"));
        saleOrderOld.getOrderLines().add(orderLine);
        Mockito.when(saleOrderDaoMock.findFullById(SALE_ORDER_ID)).thenReturn(
                saleOrderOld);

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setId(SALE_ORDER_ID);
        saleOrder.setCustomer(customer1);
        saleOrder.getOrderLines().add(orderLine);
        saleOrderService.save(saleOrder);

        assertEquals(Integer.valueOf(3), product.getQuantity());
        assertEquals(0,
                new BigDecimal("79").compareTo(customer2.getCurrentCredit()));
        assertEquals(0,
                new BigDecimal("70").compareTo(customer1.getCurrentCredit()));

    }

    @Test(expected = CustomerCreditLimitReachedException.class)
    public void testSave3() {
        final int CUSTOMER_ID = 10;
        final int PRODUCT_ID = 333;

        CustomerDao customerDaoMock = Mockito.mock(CustomerDao.class);
        saleOrderService.setCustomerDao(customerDaoMock);

        ProductDao productDaoMock = Mockito.mock(ProductDao.class);
        saleOrderService.setProductDao(productDaoMock);

        SaleOrderDao saleOrderDaoMock = Mockito.mock(SaleOrderDao.class);
        saleOrderService.setSaleOrderDao(saleOrderDaoMock);

        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setCreditLimit(new BigDecimal("100"));
        customer.setCurrentCredit(new BigDecimal("90"));
        Mockito.when(customerDaoMock.findById(CUSTOMER_ID))
                .thenReturn(customer);

        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setQuantity(3);
        Mockito.when(productDaoMock.findById(PRODUCT_ID)).thenReturn(product);

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomer(customer);
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(2);
        orderLine.setPrice(new BigDecimal("10"));
        saleOrder.getOrderLines().add(orderLine);
        saleOrderService.save(saleOrder);

    }

    @Test(expected = OutOfProductException.class)
    public void testSave4() {
        final int CUSTOMER_ID = 10;
        final int PRODUCT_ID = 333;

        CustomerDao customerDaoMock = Mockito.mock(CustomerDao.class);
        saleOrderService.setCustomerDao(customerDaoMock);

        ProductDao productDaoMock = Mockito.mock(ProductDao.class);
        saleOrderService.setProductDao(productDaoMock);

        SaleOrderDao saleOrderDaoMock = Mockito.mock(SaleOrderDao.class);
        saleOrderService.setSaleOrderDao(saleOrderDaoMock);

        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setCreditLimit(new BigDecimal("100"));
        customer.setCurrentCredit(new BigDecimal("50"));
        Mockito.when(customerDaoMock.findById(CUSTOMER_ID))
                .thenReturn(customer);

        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setQuantity(3);
        Mockito.when(productDaoMock.findById(PRODUCT_ID)).thenReturn(product);

        SaleOrder saleOrder = new SaleOrder();
        saleOrder.setCustomer(customer);
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(4);
        orderLine.setPrice(new BigDecimal("10"));
        saleOrder.getOrderLines().add(orderLine);
        saleOrderService.save(saleOrder);

    }

    
    @Test
    public void testDelete1() {
        final int CUSTOMER_ID = 10;
        final int PRODUCT_ID = 333;
        final int SALE_ORDER_ID = 35454;

        CustomerDao customerDaoMock = Mockito.mock(CustomerDao.class);
        saleOrderService.setCustomerDao(customerDaoMock);

        ProductDao productDaoMock = Mockito.mock(ProductDao.class);
        saleOrderService.setProductDao(productDaoMock);

        SaleOrderDao saleOrderDaoMock = Mockito.mock(SaleOrderDao.class);
        saleOrderService.setSaleOrderDao(saleOrderDaoMock);

        Customer customer = new Customer();
        customer.setId(CUSTOMER_ID);
        customer.setCreditLimit(new BigDecimal("100"));
        customer.setCurrentCredit(new BigDecimal("50"));
        Mockito.when(customerDaoMock.findById(CUSTOMER_ID)).thenReturn(
                customer);

        Product product = new Product();
        product.setId(PRODUCT_ID);
        product.setQuantity(3);
        Mockito.when(productDaoMock.findById(PRODUCT_ID)).thenReturn(product);

        SaleOrder saleOrderOld = new SaleOrder();
        saleOrderOld.setId(SALE_ORDER_ID);
        saleOrderOld.setCustomer(customer);
        OrderLine orderLine = new OrderLine();
        orderLine.setProduct(product);
        orderLine.setQuantity(2);
        orderLine.setPrice(new BigDecimal("10"));
        saleOrderOld.getOrderLines().add(orderLine);
        Mockito.when(saleOrderDaoMock.findFullById(SALE_ORDER_ID)).thenReturn(
                saleOrderOld);

        saleOrderService.delete(SALE_ORDER_ID);
        
        assertEquals(Integer.valueOf(5), product.getQuantity());
        assertEquals(0,
                new BigDecimal("30").compareTo(customer.getCurrentCredit()));

    }

}
