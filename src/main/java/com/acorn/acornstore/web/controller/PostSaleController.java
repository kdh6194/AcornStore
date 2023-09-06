package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.service.ProductSaleService;
import com.acorn.acornstore.web.dto.ProductSaleReqDto;
import com.acorn.acornstore.web.dto.ProductSaleResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products/sale")
public class PostSaleController {
    private final ProductSaleService productSaleService;

    // 쇼핑몰 판매 물품 등록
    @PostMapping("/register")
    public ResponseEntity<ProductSaleResDto> registerProductSale(
            ProductSaleReqDto productSaleReqDto ,
            @AuthenticationPrincipal UserDetails userDetails) { // 유저 추가
        System.out.println("유저 뭐로 가져옴?"+userDetails);


        ProductSaleResDto result = productSaleService.registerProductSale(userDetails.getUsername(), productSaleReqDto);


        return ResponseEntity.ok(result);

    }
}
