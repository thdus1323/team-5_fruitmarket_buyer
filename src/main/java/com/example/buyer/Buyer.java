package com.example.buyer;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Table(name = "buyer_tb")
@Entity
public class Buyer {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer buyerId;

    @Column(unique = true, length = 20, nullable = false)
    private String buyerName;

    @Column(nullable = false)
    private String buyerPw;

    @Column(nullable = false)
    private String buyerEmail;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Buyer(Integer buyerId, String buyerName, String buyerPw, String buyerEmail, LocalDateTime createdAt) {
        this.buyerId = buyerId;
        this.buyerName = buyerName;
        this.buyerPw = buyerPw;
        this.buyerEmail = buyerEmail;
        this.createdAt = createdAt;
    }
}

