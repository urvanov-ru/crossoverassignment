package ru.urvanov.crossover.salesorder.dao;

import java.util.List;

import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.domain.SaleOrderListItem;

public interface SaleOrderDao {

    SaleOrder findById(Integer id);

    SaleOrder findFullByNumber(String number);
    
    SaleOrder findFullById(Integer id);

    List<SaleOrder> findAll();

    void save(SaleOrder saleOrder);

    void delete(Integer id);

    List<SaleOrderListItem> list();

}
