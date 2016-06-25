package ru.urvanov.crossover.salesorder.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.domain.Customer_;
import ru.urvanov.crossover.salesorder.domain.OrderLine;
import ru.urvanov.crossover.salesorder.domain.OrderLine_;
import ru.urvanov.crossover.salesorder.domain.SaleOrder;
import ru.urvanov.crossover.salesorder.domain.SaleOrderListItem;
import ru.urvanov.crossover.salesorder.domain.SaleOrder_;

@Repository("saleOrderDao")
public class SaleOrderDaoImpl implements SaleOrderDao {

    private static final Logger logger = LoggerFactory
            .getLogger(SaleOrderDaoImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public SaleOrder findById(Integer id) {
        return em.find(SaleOrder.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public SaleOrder findFullByNumber(String number) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SaleOrder> criteriaQuery = criteriaBuilder
                .createQuery(SaleOrder.class);
        Root<SaleOrder> rootSaleOrder = criteriaQuery.from(SaleOrder.class);
        criteriaQuery.where(criteriaBuilder.equal(
                rootSaleOrder.get(SaleOrder_.number), number));
        TypedQuery<SaleOrder> typedQuery = em.createQuery(criteriaQuery);
        List<SaleOrder> result = typedQuery.getResultList();
        if (result.size() > 0) {
            logger.debug("saleOrder.orderLines.size = {}", result.get(0)
                    .getOrderLines().size());
            return result.get(0);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public SaleOrder findFullById(Integer id) {
        SaleOrder saleOrder = em.find(SaleOrder.class, id);
        logger.debug("saleOrder.orderLines.size={}", saleOrder.getOrderLines()
                .size());
        return saleOrder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SaleOrder> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<SaleOrder> criteriaQuery = criteriaBuilder
                .createQuery(SaleOrder.class);
        criteriaQuery.from(SaleOrder.class);
        TypedQuery<SaleOrder> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional
    @Override
    public void save(SaleOrder saleOrder) {
        if (saleOrder.getId() == null) {
            em.persist(saleOrder);
        } else {
            em.merge(saleOrder);
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        SaleOrder saleOrder = em.find(SaleOrder.class, id);
        em.remove(saleOrder);
    }

    @Override
    public List<SaleOrderListItem> list() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<SaleOrder> rootSaleOrder = criteriaQuery.from(SaleOrder.class);
        Join<SaleOrder, Customer> joinCustomer = rootSaleOrder
                .join(SaleOrder_.customer);
        Join<SaleOrder, OrderLine> joinOrderLine = rootSaleOrder.join(
                SaleOrder_.orderLines, JoinType.LEFT);
        criteriaQuery.groupBy(rootSaleOrder.get(SaleOrder_.id),
                joinCustomer.get(Customer_.code),
                joinCustomer.get(Customer_.name));

        criteriaQuery.multiselect(
                rootSaleOrder.get(SaleOrder_.id),
                rootSaleOrder.get(SaleOrder_.number),
                joinCustomer.get(Customer_.code),
                joinCustomer.get(Customer_.name),
                criteriaBuilder.sum(criteriaBuilder.prod(
                        joinOrderLine.get(OrderLine_.quantity),
                        joinOrderLine.get(OrderLine_.price))));

        criteriaQuery.orderBy(criteriaBuilder.asc(rootSaleOrder
                .get(SaleOrder_.number)));
        TypedQuery<Tuple> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery
                .getResultList()
                .stream()
                .map(o -> {
                    SaleOrderListItem item = new SaleOrderListItem();
                    item.setId((Integer) o.get(0));
                    item.setNumber((String) o.get(1));
                    item.setCustomerCode((String) o.get(2));
                    item.setCustomerName((String) o.get(3));
                    item.setTotalPrice((BigDecimal) (o.get(4) == null ? BigDecimal.ZERO
                            : o.get(4)));
                    return item;
                }).collect(Collectors.toList());
    }

}
