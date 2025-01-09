package com.kaminsky.managerapp.repository;

import com.kaminsky.managerapp.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
}
