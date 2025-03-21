package com.kaminsky.customerapp.client;

import com.kaminsky.customerapp.entity.Product;
import reactor.core.publisher.Flux;

public interface ProductsClient {

    Flux<Product> findAllProducts(String filter);
}
