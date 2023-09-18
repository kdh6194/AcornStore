package com.acorn.acornstore.service;

import org.springframework.data.domain.*;

import com.acorn.acornstore.domain.Product;
import com.acorn.acornstore.domain.repository.ProductRepository;
import com.acorn.acornstore.web.dto.ProductSaleResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShowProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<Product> showList(int page){
        int size = 10;

        Sort sort = Sort.by("productId").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Product> showView(Long id){
        return productRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public Page<Product> searchByProductName(String productName,int page){
        int size = 10;
        Sort sort = Sort.by("productId").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findByProductNameContaining(productName, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Product> filterByCategory(String categoryName,int page){
        int size = 10;
        Sort sort = Sort.by("productId").ascending();
        Pageable pageable = PageRequest.of(page,size ,sort);

        return productRepository.findByCategoryName(categoryName ,pageable);
    }

    @Transactional(readOnly=true)
    public Page<Product> searchAndFilter(String productName,String categoryName,int page){
        int size=10;
        Sort sort=Sort.by("productId").ascending();
        Pageable pageable=PageRequest.of(page,size ,sort);

        return productRepository.findByProductNameContainingAndCategoryName(productName ,categoryName ,pageable);
    }
}
