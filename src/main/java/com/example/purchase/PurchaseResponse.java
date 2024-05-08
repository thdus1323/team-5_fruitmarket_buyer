package com.example.purchase;

import lombok.Data;

public class PurchaseResponse {
    @Data
    public class ListDTO {
        //목록구현하기
        private Integer buyerId;
        private Integer purQty;

        public ListDTO(Purchase purchase) {
            this.buyerId = purchase.getBuyerId();
            this.purQty = purchase.getPurQty();
        }
    }
}