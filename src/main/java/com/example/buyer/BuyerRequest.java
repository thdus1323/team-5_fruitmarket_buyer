package com.example.buyer;

import com.example.purchase.Purchase;
import lombok.Data;

public class BuyerRequest {

    @Data
    public static class JoinDTO{
        private String buyerName;
        private String buyerPw;
        private String buyerEmail;

    }

    @Data
    public static class LoginDTO{
        private String buyerName;
        private String buyerPw;
    }

}
