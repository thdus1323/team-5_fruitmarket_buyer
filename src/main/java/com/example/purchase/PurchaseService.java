package com.example.purchase;

import com.example.buyer.Buyer;
import com.example.buyer.BuyerRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final BuyerRepository buyerRepository;
    private final HttpSession session;

    public Purchase findById(Integer buyerId) {
        Purchase purchase = purchaseRepository.findById(buyerId);
        return purchase;
    }

    //구매하기
    @Transactional
    public void savePurchase(Integer buyerId, PurchaseRequest.SaveDTO reqDTO){

        //주문한 사람(sessionUser) 의 정보를 찾아옴
        //BuyerRepository에
        Buyer buyer = buyerRepository.findByBuyerId(buyerId);
        // 구매 정보를 저장하는 등의 로직 수행
        purchaseRepository.save(buyerId, buyer.getBuyerName(), reqDTO);

        //업데이트
        purchaseRepository.updateQty(reqDTO);
    }

    //내구매목록보기
    public List<Purchase> getPurchaseList(Integer buyerId){
        List<Purchase> purchaseList = purchaseRepository.findByBuyerId(buyerId);
        return purchaseList;
    }

    //구매수량 수정하기
    @Transactional
    public void changePurQty(Integer buyerId, PurchaseRequest.UpdateDTO reqDTO){
        purchaseRepository.updateById(buyerId, reqDTO);
    }

}
