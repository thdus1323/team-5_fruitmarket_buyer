package com.example.buyer;

import com.example.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BuyerRepository {
    private final EntityManager em;

    public Buyer findByBuyerId(Integer buyerId){
        Query query = em.createNativeQuery("select * from buyer_tb where buyer_id = ?", Buyer.class);
        query.setParameter(1, buyerId);
        Buyer buyer = (Buyer) query.getSingleResult();
        return buyer;
    }

    //회원가입
    public void join(BuyerRequest.JoinDTO reqDTO) {
        Query query = em.createNativeQuery(
                """
                        insert into buyer_tb(buyer_name, buyer_pw, buyer_email, created_at) values(?,?,?,now())
                        """);
        query.setParameter(1, reqDTO.getBuyerName());
        query.setParameter(2, reqDTO.getBuyerPw());
        query.setParameter(3, reqDTO.getBuyerEmail());
        query.executeUpdate();
    }

    //로그인
    public Buyer login(BuyerRequest.LoginDTO reqDTO) {
        Query query = em.createNativeQuery("select * from buyer_tb where buyer_name=? and buyer_pw=?", Buyer.class);
        query.setParameter(1, reqDTO.getBuyerName());
        query.setParameter(2, reqDTO.getBuyerPw());
        Buyer sessionBuyer = (Buyer) query.getSingleResult();
        return sessionBuyer;
    }


}

