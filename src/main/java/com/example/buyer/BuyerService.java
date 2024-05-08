package com.example.buyer;

import com.example.product.Product;
import com.example.product.ProductRepository;
import com.example.product.ProductResponse;
import jakarta.persistence.Table;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuyerService {
    @Autowired
    private BuyerRepository buyerRepository;

    //회원가입
    @Transactional
    public void joinByNameAndPwAndEmail(BuyerRequest.JoinDTO reqDTO){
        buyerRepository.join(reqDTO);
    }

    //로그인
    public Buyer loginByNameAndPw(BuyerRequest.LoginDTO reqDTO){
        Buyer sessionBuyer = buyerRepository.login(reqDTO);
        return sessionBuyer;
    }




}
