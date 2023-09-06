package com.acorn.acornstore.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String categoryName;
    private int productPrice;
    private int productQuantity;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductImage> images = new ArrayList<>();  //productImages;

    private String productDetail;
    //private String productOnSale;
    private LocalDate closingAt; // 판매 종료

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;  // 등록한 멤버 (판매자 또는 구매자)

    // @OneToMany(mappedBy="product", cascade=CascadeType.ALL)
    // private List<Order> orders;  // 해당 상품에 대한 모든 주문들

    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
