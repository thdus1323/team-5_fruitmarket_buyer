package com.example.purchase;

import com.example.buyer.Buyer;
import com.example.product.Product;
import com.example.product.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class PurchaseController {
    private final PurchaseService purchaseService;
    private final ProductService productService;
    private final HttpSession session;

    //상품 결정하기 폼
    @GetMapping("/purchase/{productId}/purchase-form")
    public String purchaseForm(@PathVariable Integer productId, HttpServletRequest request) {
        Product product = productService.findById(productId);
        System.out.println("product =ererer " + product);
        request.setAttribute("product", product);
        return "purchase/purchase-form";
    }
    //상품 구매 결정하기
    @PostMapping("/purchase/save")
    public String saveByBuyerName(PurchaseRequest.SaveDTO reqDTO) {
        Buyer sessionBuyer = (Buyer) session.getAttribute("sessionBuyer");

        System.out.println("이거는 상품 구매하기 디티오입니다!! " + reqDTO);
        System.out.println("이것은 세션유저입니다.! 출력되는지 확인 중입니다. " + sessionBuyer.getBuyerId() + sessionBuyer.getBuyerName());

        purchaseService.savePurchase(sessionBuyer.getBuyerId(), reqDTO);


        return "redirect:/purchase/list";
    }


}

