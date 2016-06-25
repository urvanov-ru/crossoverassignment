package com.dev.frontend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.domain.Product;
import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.service.CustomerService;
import ru.urvanov.crossover.salesorder.service.ProductService;
import ru.urvanov.crossover.salesorder.service.SaleOrderService;

import com.dev.frontend.panels.ComboBoxItem;

public class Services {
    public static final int TYPE_PRODUCT = 1;
    public static final int TYPE_CUSTOMER = 2;
    public static final int TYPE_SALESORDER = 3;

    private static ApplicationContext applicationContext;
    private static CustomerService customerService;
    private static ProductService productService;
    private static SaleOrderService saleOrderService;

    static {
        applicationContext = new GenericXmlApplicationContext(
                "classpath:applicationContext.xml");
        customerService = (CustomerService) applicationContext
                .getBean("customerService");
        productService = (ProductService) applicationContext
                .getBean("productService");
        saleOrderService = (SaleOrderService) applicationContext
                .getBean("saleOrderService");
    }

    public static Object save(Object object, int objectType) {
        // TODO by the candidate
        /*
         * This method is called eventually after you click save on any edit
         * screen object parameter is the return object from calling method
         * guiToObject on edit screen and the type is identifier of the object
         * type and may be TYPE_PRODUCT , TYPE_CUSTOMER or TYPE_SALESORDER
         */
        switch (objectType) {
        case TYPE_PRODUCT:
            Product product = (Product) object;
            productService.save(product);
            return product;
        case TYPE_CUSTOMER:
            Customer customer = (Customer) object;
            customerService.save(customer);
            return customer;
        case TYPE_SALESORDER:
            SaleOrder saleOrder = (SaleOrder) object;
            saleOrderService.save(saleOrder);
            return saleOrder;
        }
        return null;
    }

    public static Object readRecordByCode(String code, int objectType) {
        // TODO by the candidate
        /*
         * This method is called when you select record in list view of any
         * entity and also called after you save a record to re-bind the record
         * again the code parameter is the first column of the row you have
         * selected and the type is identifier of the object type and may be
         * TYPE_PRODUCT , TYPE_CUSTOMER or TYPE_SALESORDER
         */
        switch (objectType) {
        case TYPE_PRODUCT:
            return productService.findByCode(code);
        case TYPE_CUSTOMER:
             return customerService.findByCode(code);
        case TYPE_SALESORDER:
            return saleOrderService.findFullByNumber(code);
        }
        return null;
    }

    public static boolean deleteRecordByCode(String code, int objectType) {
        // TODO by the candidate
        /*
         * This method is called when you click delete button on an edit view
         * the code parameter is the code of (Customer - PRoduct ) or order
         * number of Sales Order and the type is identifier of the object type
         * and may be TYPE_PRODUCT , TYPE_CUSTOMER or TYPE_SALESORDER
         */
        switch (objectType) {
        case TYPE_PRODUCT:
            Product product = productService.findByCode(code);
            if (product != null) productService.delete(product.getId());
            return true;
        case TYPE_CUSTOMER:
            Customer customer = customerService.findByCode(code);
            if (customer != null) customerService.delete(customer.getId());
            return true;
        case TYPE_SALESORDER:
            SaleOrder saleOrder = saleOrderService.findFullByNumber(code);
            if (saleOrder != null) saleOrderService.delete(saleOrder.getId());
            return true;
        }
        return false;
    }

    public static List<Object> listCurrentRecords(int objectType) {
        // TODO by the candidate
        /*
         * This method is called when you open any list screen and should return
         * all records of the specified type
         */
        List<Object> result = new ArrayList<>();
        switch (objectType) {
        case TYPE_CUSTOMER:
            result.addAll(customerService.findAll());
            break;
        case TYPE_PRODUCT:
            result.addAll(productService.findAll());
            break;
        case TYPE_SALESORDER:
             result.addAll(saleOrderService.list());
             break;
        }
        return result;
    }

    public static List<ComboBoxItem> listCurrentRecordRefernces(int objectType) {
        // TODO by the candidate
        /*
         * This method is called when a Combo Box need to be initialized and
         * should return list of ComboBoxItem which contains code and
         * description/name for all records of specified type
         */
        List<ComboBoxItem> result =  new ArrayList<ComboBoxItem>();
        switch (objectType) {
        case TYPE_PRODUCT:
            return productService.findAll().stream().map(o->{
                ComboBoxItem item = new ComboBoxItem();
                item.setKey(o.getCode());
                item.setValue(o.getDescription());
                return item;
            }).collect(Collectors.toList());
        case TYPE_CUSTOMER:
            return customerService.findAll().stream().map(o->{
                ComboBoxItem item = new ComboBoxItem();
                item.setKey(o.getCode());
                item.setValue(o.getName());
                return item;
            }).collect(Collectors.toList());
        }
        return result;
    }

    public static double getProductPrice(String productCode) {
        // TODO by the candidate
        /*
         * This method is used to get unit price of product with the code passed
         * as a parameter
         */
        Product product = productService.findByCode(productCode);
        return product.getPrice().doubleValue();
    }
}
