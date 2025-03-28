package com.kaminsky.customerapp.controller;

import com.kaminsky.customerapp.client.ProductsClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products")
public class ProductsController {
    private ProductsClient productsClient;

    @GetMapping("list")
    public Mono<String> getProductsListPage(Model model, @RequestParam(name = "filter", required = false) String filter) {
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customer/products/list");

    }
}
