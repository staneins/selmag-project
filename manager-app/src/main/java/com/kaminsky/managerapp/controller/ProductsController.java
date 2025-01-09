package com.kaminsky.managerapp.controller;

import com.kaminsky.managerapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductsController {
    private final ProductService productService;
}
