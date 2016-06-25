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

import ru.urvanov.crossover.salesorder.domain.Product;
import ru.urvanov.crossover.salesorder.domain.Product_;

@Repository("productDao")
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public Product findById(Integer id) {
        return em.find(Product.class, id);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Product findByCode(String code) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder
                .createQuery(Product.class);
        Root<Product> rootProduct = criteriaQuery.from(Product.class);
        criteriaQuery.where(criteriaBuilder.equal(rootProduct.get(Product_.code),
                code));
        TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
        List<Product> result = typedQuery.getResultList();
        if (result.size() > 0) {
            return result.get(0);
        }
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Product> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteriaQuery = criteriaBuilder
                .createQuery(Product.class);
        criteriaQuery.from(Product.class);
        TypedQuery<Product> typedQuery = em.createQuery(criteriaQuery);
        return typedQuery.getResultList();
    }

    @Transactional
    @Override
    public void save(Product product) {
        if (product.getId() == null) {
            em.persist(product);
        } else {
            em.merge(product);
        }
    }

    @Transactional
    @Override
    public void delete(Integer id) {
        Product product = em.find(Product.class, id);
        em.remove(product);
    }

}
