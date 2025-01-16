package com.kaminsky.controller;

import com.kaminsky.controller.payload.NewProductPayload;
import com.kaminsky.entity.Product;
import com.kaminsky.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;


    @GetMapping
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter,
                                          Principal principal) {
        LoggerFactory.getLogger(ProductsRestController.class).info("Principal name: {}", principal.getName());
        return this.productService.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody NewProductPayload payload, BindingResult bindingResult, UriComponentsBuilder builder) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException e) {
                throw e;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity.created(builder.replacePath("/catalagoue-api/products/{productId}").build(Map.of("productId", product.getId()))).body(product);
        }
    }
}
