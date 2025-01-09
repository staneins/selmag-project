package com.kaminsky.managerapp.controller;

import com.kaminsky.managerapp.entity.Product;
import com.kaminsky.managerapp.payload.UpdateProductPayload;
import com.kaminsky.managerapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
    private final ProductService productService;

    @GetMapping()
    public String getProduct(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("product", this.productService.findProduct(productId).orElseThrow());
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("product", this.productService.findProduct(productId).orElseThrow());
        return "catalogue/products/edit";
    }


    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId, Model model) {
        return this.productService.findProduct(productId).orElseThrow();
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload) {
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }
}
