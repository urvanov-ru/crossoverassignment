package ru.urvanov.crossover.salesorder.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.crossover.salesorder.dao.CustomerDao;
import ru.urvanov.crossover.salesorder.dao.ProductDao;
import ru.urvanov.crossover.salesorder.dao.SaleOrderDao;
import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.domain.OrderLine;
import ru.urvanov.crossover.salesorder.domain.Product;
import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.domain.SaleOrderListItem;
import ru.urvanov.crossover.salesorder.exception.CustomerCreditLimitReachedException;
import ru.urvanov.crossover.salesorder.exception.OutOfProductException;

@Service(value = "saleOrderService")
public class SaleOrderServiceImpl implements SaleOrderService {

    @Autowired
    private SaleOrderDao saleOrderDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private ProductDao productDao;

    @Override
    public SaleOrder findById(Integer id) {
        return saleOrderDao.findById(id);
    }

    @Override
    public SaleOrder findFullByNumber(String number) {
        SaleOrder saleOrder = saleOrderDao.findFullByNumber(number);
        return saleOrder;
    }

    @Override
    public SaleOrder findFullById(Integer id) {
        SaleOrder saleOrder = saleOrderDao.findFullById(id);
        return saleOrder;
    }
    
    @Override
    public List<SaleOrder> findAll() {
        return saleOrderDao.findAll();
    }

    @Transactional
    @Override
    public void save(SaleOrder saleOrder) {
        class ProductWithNewQuantity {
            private Product product;
            private Integer quantity;

            public Product getProduct() {
                return product;
            }

            public void setProduct(Product product) {
                this.product = product;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

        }
        Map<Integer, ProductWithNewQuantity> mapProducts = new HashMap<>();
        Customer customerNew = customerDao.findById(saleOrder.getCustomer()
                .getId());
        BigDecimal currentCreditNew = customerNew.getCurrentCredit();
        Customer customerOld = null;
        if (saleOrder.getId() != null) {
            SaleOrder saleOrderOld = findFullById(saleOrder.getId());
            BigDecimal totalPriceOld = BigDecimal.ZERO;
            totalPriceOld = calculateTotalPrice(saleOrderOld);
            if (!saleOrderOld.getCustomer().equals(saleOrder.getCustomer())) {
                // Correct current credit.
                customerOld = customerDao.findById(saleOrderOld.getCustomer()
                        .getId());
                customerOld.setCurrentCredit(customerOld.getCurrentCredit()
                        .subtract(totalPriceOld));
            } else {
                currentCreditNew = currentCreditNew.subtract(totalPriceOld);
            }

            // correct Product quantities to state without this sale order.
            for (OrderLine orderLineOld : saleOrderOld.getOrderLines()) {
                ProductWithNewQuantity productWithNewQuantity = mapProducts
                        .get(orderLineOld.getProduct().getId());
                if (productWithNewQuantity == null) {
                    productWithNewQuantity = new ProductWithNewQuantity();
                    productWithNewQuantity.setProduct(productDao
                            .findById(orderLineOld.getProduct().getId()));
                    productWithNewQuantity.setQuantity(productWithNewQuantity
                            .getProduct().getQuantity());
                    mapProducts.put(
                            productWithNewQuantity.getProduct().getId(),
                            productWithNewQuantity);
                }
                productWithNewQuantity.setQuantity(productWithNewQuantity
                        .getQuantity() + orderLineOld.getQuantity());
            }
        }

        BigDecimal totalPriceNew = calculateTotalPrice(saleOrder);
        currentCreditNew = currentCreditNew.add(totalPriceNew);
        if (currentCreditNew.compareTo(customerNew.getCreditLimit()) > 0)
            throw new CustomerCreditLimitReachedException();

        // correct product quantities.
        for (OrderLine orderLine : saleOrder.getOrderLines()) {
            ProductWithNewQuantity productWithNewQuantity = mapProducts
                    .get(orderLine.getProduct().getId());
            if (productWithNewQuantity == null) {
                productWithNewQuantity = new ProductWithNewQuantity();
                productWithNewQuantity.setProduct(productDao.findById(orderLine
                        .getProduct().getId()));
                productWithNewQuantity.setQuantity(productWithNewQuantity
                        .getProduct().getQuantity());
                mapProducts.put(productWithNewQuantity.getProduct().getId(),
                        productWithNewQuantity);
            }
            productWithNewQuantity.setQuantity(productWithNewQuantity
                    .getQuantity() - orderLine.getQuantity());
            if (productWithNewQuantity.getQuantity() < 0)
                throw new OutOfProductException(productWithNewQuantity
                        .getProduct().getCode());
        }

        // save
        if (currentCreditNew.compareTo(customerNew.getCurrentCredit()) != 0) {
            customerNew.setCurrentCredit(currentCreditNew);
            customerDao.save(customerNew);
        }
        if (customerOld != null) {
            customerDao.save(customerOld);
        }
        for (ProductWithNewQuantity p : mapProducts.values()) {
            if (p.getProduct().getQuantity().compareTo(p.getQuantity()) != 0) {
                p.getProduct().setQuantity(p.getQuantity());
                productDao.save(p.getProduct());
            }
        }
        saleOrderDao.save(saleOrder);
    }

    private BigDecimal calculateTotalPrice(SaleOrder saleOrderFull) {
        return saleOrderFull
                .getOrderLines()
                .stream()
                .map(o -> o.getPrice()
                        .multiply(new BigDecimal(o.getQuantity())))
                .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        SaleOrder saleOrder = findFullById(id);
        BigDecimal totalPrice = calculateTotalPrice(saleOrder);
        Customer customer = customerDao.findById(saleOrder.getCustomer().getId());
        customer.setCurrentCredit(customer.getCurrentCredit().subtract(totalPrice));
        
        for (OrderLine orderLine : saleOrder.getOrderLines()) {
            Product product = productDao.findById(orderLine.getProduct().getId());
            product.setQuantity(product.getQuantity() + orderLine.getQuantity());
            productDao.save(product);
        }
        customerDao.save(customer);
        
        saleOrderDao.delete(id);
    }

    @Override
    public List<SaleOrderListItem> list() {
        return saleOrderDao.list();
    }

    public SaleOrderDao getSaleOrderDao() {
        return saleOrderDao;
    }

    public void setSaleOrderDao(SaleOrderDao saleOrderDao) {
        this.saleOrderDao = saleOrderDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

}
