package com.kaminsky.managerapp.controller;

import com.kaminsky.managerapp.client.BadRequestException;
import com.kaminsky.managerapp.client.ProductRestClient;
import com.kaminsky.managerapp.controller.payload.NewProductPayload;
import com.kaminsky.managerapp.entity.Product;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {
    private final ProductRestClient productRestClient;

    @GetMapping("list")
    public String getProductsList(Model model, @RequestParam(name = "filter", required = false) String filter, Principal principal) {
        LoggerFactory.getLogger(ProductsController.class).info("User: {}", principal);
        model.addAttribute("products", this.productRestClient.findAllProducts(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getNewProductsPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("create")
    public String createProduct(NewProductPayload payload, Model model) {
            try {
                Product product = this.productRestClient.createProduct(payload.title(), payload.details());
                return "redirect:/catalogue/products/%d".formatted(product.id());
            } catch (BadRequestException e) {
                model.addAttribute("payload", payload);
                model.addAttribute("errors", e.getErrors());
                return "catalogue/products/new_product";
            }
        }
}
