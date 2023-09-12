package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.service.ProductSaleService;
import com.acorn.acornstore.web.dto.ProductSaleReqDto;
import com.acorn.acornstore.web.dto.ProductSaleResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products/sale")
public class ProductSaleController {
    private final ProductSaleService productSaleService;

    // 쇼핑몰 판매 물품 등록
    @PostMapping("/register")
    public ResponseEntity<ProductSaleResDto> registerProductSale(
            ProductSaleReqDto productSaleReqDto,
            @AuthenticationPrincipal UserDetails userDetails) { // 유저 추가
        System.out.println("유저 뭐로 가져옴?"+userDetails.getUsername());
        String email = "test@naver.com";


        ProductSaleResDto result = productSaleService.registerProductSale(userDetails.getUsername()/* email*/, productSaleReqDto);


        return ResponseEntity.ok(result);

    }


    // 쇼핑몰 판매 물품 조회 (판매 중인 물품만)
    @GetMapping("/myproducts")
    public ResponseEntity<List<ProductSaleResDto>> getMyProductsOnSale(
            @AuthenticationPrincipal UserDetails userDetails) {


        List<ProductSaleResDto> products = productSaleService.getProductsOnSale(userDetails.getUsername(), true);
        System.out.println("조회중");
        if (products.isEmpty()) {
            // 물품이 없는 경우에 대한 처리
            String message = "판매 중인 물품이 없습니다."; // 원하는 메시지로 변경
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header("message", message)
                    .body(products);
        }

        return ResponseEntity.ok(products);
    }



    @GetMapping("/mysoldproducts")
    public ResponseEntity<List<ProductSaleResDto>> getMySoldProducts(
            @AuthenticationPrincipal UserDetails userDetails) {
        List<ProductSaleResDto> products = productSaleService.getProductsOnSale(userDetails.getUsername(), false);
        if (products.isEmpty()) {
            // 물품이 없는 경우에 대한 처리
            String message = "판매했던 물품이 없습니다."; // 원하는 메시지로 변경
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .header("message", message)
                    .body(products);
        }

        return ResponseEntity.ok(products);
    }

    // 재고 수정 엔드포인트 추가
    @PatchMapping("/{productId}/updateQuantity")
    public ResponseEntity<ProductSaleResDto> updateProductQuantity(
            @PathVariable Long productId,
            @RequestBody ProductSaleReqDto productSaleReqDto,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 재고 수정 서비스
        ProductSaleResDto updatedProduct = productSaleService.updateProductQuantity(productId, productSaleReqDto.getProductQuantity(), userDetails.getUsername());

        if (updatedProduct != null) {
            return ResponseEntity.ok(updatedProduct);
        } else {
            String message = "재고 수정 실패.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header("message", message)
                    .build();
        }
    }


    // 상품 상태 업데이트 (판매 종료 날짜가 지난 상품의 상태를 CLOSED로 변경)
    @PatchMapping("/updateStatus")
    public ResponseEntity<Void> updateProductStatus() {
        productSaleService.updateProductStatus();
        return ResponseEntity.noContent().build();
    }





}
