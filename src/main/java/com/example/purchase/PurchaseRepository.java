package com.example.purchase;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PurchaseRepository {
    private final EntityManager em;

    //구매하기
    public Purchase findById(Integer productId) {
        Query query = em.createNativeQuery("select * from purchase_tb where product_id=?", Purchase.class);
        query.setParameter(1,productId);
        return (Purchase) query.getSingleResult();
    }


    //상품구매목록 조회
    public List<Purchase> findByBuyerId(Integer buyerId) {
        String q = """
                select * from purchase_tb where buyer_id = ?
                """;
        Query query = em.createNativeQuery(q, Purchase.class);
        query.setParameter(1, buyerId);
        List<Purchase> purchaseList = query.getResultList();
        return purchaseList;
    }


    //상품구매수량
    public void updateById(Integer buyerId, PurchaseRequest.UpdateDTO reqDTO){
        Query query = em.createNativeQuery("update purchase_tb set pur_qty=? where buyer_id=?");
        query.setParameter(1, reqDTO.getPurQty());
        query.setParameter(2,buyerId);
        query.executeUpdate();

    }


    public void save(Integer buyerId, String buyerName,  PurchaseRequest.SaveDTO reqDTO) {
        String q = """
                insert into purchase_tb(buyer_id, buyer_name, product_id, product_name, product_price, product_qty, pur_qty, created_at) 
                values (?, ?, ?, ?, ?, ?, ?, now())
                """;

        Query query = em.createNativeQuery(q);
        query.setParameter(1, buyerId);
        query.setParameter(2, buyerName);
        query.setParameter(3, reqDTO.getProductId());
        query.setParameter(4, reqDTO.getProductName());
        query.setParameter(5, reqDTO.getProductPrice());
        query.setParameter(6, reqDTO.getProductQty());
        query.setParameter(7, reqDTO.getPurQty());

        query.executeUpdate();
    }

    //내 구매목록, 상품재고 수량 변경
    public void updateQty(PurchaseRequest.SaveDTO reqDTO){
        String q = """
                update product_tb set product_qty = product_qty - ? where product_id = ?
                """;
        Query query = em.createNativeQuery(q);
        query.setParameter(1, reqDTO.getPurQty());
        query.setParameter(2, reqDTO.getProductId());
        query.executeUpdate();
    }
}
