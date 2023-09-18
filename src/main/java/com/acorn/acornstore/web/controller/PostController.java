package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.domain.Product;
import com.acorn.acornstore.service.ShowProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostController {

    private final ShowProductService showProductService;

    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "") String name,
                       @RequestParam(defaultValue = "") String category,
                       @RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> products;

        if (!name.isEmpty() && !category.isEmpty()) {
            products = showProductService.searchAndFilter(name, category, page);
        } else if (!name.isEmpty()) {
            products = showProductService.searchByProductName(name, page);
        } else if (!category.isEmpty()) {
            products = showProductService.filterByCategory(category, page);
        } else {
            products = showProductService.showList(page);
        }


        if (page < 0 || page >= products.getTotalPages()) {
            return "redirect:/api/list"; // 유효하지 않다면 첫 번째 페이지로 리다이렉트
        }

        List<Product> filteredProducts = products.getContent().stream()
                .filter(product -> product.getProductQuantity() > 0)
                .collect(Collectors.toList());

        for (Product product : filteredProducts) {
            Long productId = product.getProductId();
            String productName = product.getProductName();
        }

        model.addAttribute("products", filteredProducts);
        model.addAttribute("page", products);

        return "list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model) {
        Product product = showProductService.showView(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        return "view";
    }

    @GetMapping("/order")
    public String order(@AuthenticationPrincipal String userId, Model model) {

        return "order";
    }

}
