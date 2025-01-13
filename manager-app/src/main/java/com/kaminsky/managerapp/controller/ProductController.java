package com.kaminsky.managerapp.controller;

import com.kaminsky.managerapp.client.BadRequestException;
import com.kaminsky.managerapp.client.ProductRestClient;
import com.kaminsky.managerapp.controller.payload.UpdateProductPayload;
import com.kaminsky.managerapp.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
    private final ProductRestClient productRestClient;

    private final MessageSource messageSource;

    @GetMapping()
    public String getProduct(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("product", this.productRestClient.findProduct(productId).orElseThrow());
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage(@PathVariable("productId") int productId, Model model) {
        model.addAttribute("product", this.productRestClient.findProduct(productId).orElseThrow());
        return "catalogue/products/edit";
    }


    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId, Model model) {
        return this.productRestClient.findProduct(productId).orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute(value = "product", binding = false) Product product, UpdateProductPayload payload, Model model) {
        try {
            this.productRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", e.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product, UpdateProductPayload payload) {
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException e, Model model,
                                               HttpServletResponse response, Locale locale) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", this.messageSource.getMessage(e.getMessage(), new Object[0], e.getMessage(), locale));
        return "errors/404";
    }
}
