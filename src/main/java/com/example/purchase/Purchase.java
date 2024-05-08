package com.example.purchase;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "purchase_tb")
@Entity
public class Purchase {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer purId;  //pk

    @Column
    private Integer buyerId;    //session id (buyer의 pk키)

    @Column
    private String buyerName;  //구매 ssar love cos

    @Column
    private Integer productId;  //상품의 pk

    @Column
    private String productName;     //상품명

    @Column
    private Integer productPrice;   //상품 가격

    @Column(nullable = false)
    private Integer productQty;     //상품 재고

    @Column(nullable = false)
    private Integer purQty;         //구매 수량

    @CreationTimestamp
    private LocalDateTime createdAt;


}

