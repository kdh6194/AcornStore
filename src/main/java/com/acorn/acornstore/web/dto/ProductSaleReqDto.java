package com.acorn.acornstore.web.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProductSaleReqDto {
    private String productName;
    private String categoryName;
    private int productPrice;
    private int productQuantity;
    private List<MultipartFile> productImageFiles;
    private String productDetail;
    private LocalDate closingAt; // 판매 종료일
    //private String productOnSale;


}
