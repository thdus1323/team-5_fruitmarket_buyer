package com.example.purchase;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Data;

public class PurchaseRequest {

    @Data
    public class SaveDTO {
        private String buyerName;
        private Integer productId;
        private String productName;
        private Integer productPrice;
        private Integer productQty;
        private Integer purQty;
    }
}
