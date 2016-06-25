package ru.urvanov.crossover.salesorder.service;

import java.util.List;

import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.domain.SaleOrderListItem;

public interface SaleOrderService {

    SaleOrder findById(Integer id);

    SaleOrder findFullByNumber(String number);
    
    SaleOrder findFullById(Integer id);

    List<SaleOrder> findAll();
    
    List<SaleOrderListItem> list();

    void save(SaleOrder saleOrder);

    void delete(Integer id);
}
