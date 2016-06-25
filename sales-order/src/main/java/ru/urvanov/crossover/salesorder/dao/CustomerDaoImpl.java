package ru.urvanov.crossover.salesorder.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.urvanov.crossover.salesorder.domain.Customer;
import ru.urvanov.crossover.salesorder.domain.Customer_;

@Repository("customerDao")
public class CustomerDaoImpl implements CustomerDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public Customer findById(Integer id) {
        return em.find(Customer.class, id);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer findByCode(String code) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder
                .createQuery(Customer.class);
        Root<Customer> customer = criteriaQuery.from(Customer.class);
        criteriaQuery.where(criteriaBuilder.equal(customer.get(Customer_.code),
                code));
        TypedQuery<Customer> typedQuery = em.createQuery(criteriaQuery);
        List<Customer> result = typedQuery.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Customer> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteriaQuery = criteriaBuilder
                .createQuery(Customer.class);
        criteriaQuery.from(Customer.class);
        TypedQuery<Customer> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional
    @Override
    public void save(Customer customer) {
        if (customer.getId() == null) {
            em.persist(customer);
        } else {
            em.merge(customer);
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Customer customer = em.find(Customer.class, id);
        em.remove(customer);
    }

}
