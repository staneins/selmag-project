package com.kaminsky.managerapp.service;

import com.kaminsky.managerapp.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findAllProducts();
}
