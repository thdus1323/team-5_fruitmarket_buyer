package com.example.product;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final EntityManager em;

    // 상품목록보기
    public List<Product> findAll() {
        Query query = em.createNativeQuery("select * from product_tb order by product_id desc", Product.class);
        return query.getResultList();
    }

    //상품상세보기
    public Product findById(int productId){
        Query query = em.createNativeQuery("select * from product_tb where product_id=?", Product.class);
        query.setParameter(1,productId);
        return (Product) query.getSingleResult();
    }

}
