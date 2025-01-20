package com.kaminsky.managerapp.controller;

import com.kaminsky.managerapp.client.BadRequestException;
import com.kaminsky.managerapp.client.ProductRestClient;
import com.kaminsky.managerapp.controller.payload.NewProductPayload;
import com.kaminsky.managerapp.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты ProductsController")
class ProductsControllerTest {

    @Mock
    ProductRestClient productRestClient;

    @InjectMocks
    ProductsController controller;

    @Test
    @DisplayName("createProduct создаст новый товар и перенаправит на страницу товара")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        //given
        var payload = new NewProductPayload("Новый товар", "Описание нового товара");
        var model = new ConcurrentModel();

        doReturn(new Product(1, "Новый товар", "Описание нового товара"))
                .when(this.productRestClient)
                .createProduct("Новый товар", "Описание нового товара");
        //when
        var result = this.controller.createProduct(payload, model);
        //then
        assertEquals("redirect:/catalogue/products/1", result);
        verify(this.productRestClient).createProduct("Новый товар", "Описание нового товара");
        verifyNoMoreInteractions(this.productRestClient);
    }

    @Test
    @DisplayName("createProduct вернёт страницу с ошибками, если запрос не валиден")
    void createProduct_RequestIsInvalid_ReturnsProductFormWithErrors() {
        //given
        var payload = new NewProductPayload("   ", null);
        var model = new ConcurrentModel();

        doThrow(new BadRequestException(List.of("Ошибка1", "Ошибка 2")))
                .when(this.productRestClient)
                .createProduct("   ", null);

        //when
        var result = this.controller.createProduct(payload, model);
        //then
        assertEquals("catalogue/products/new_product", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(List.of("Ошибка1", "Ошибка 2"), model.getAttribute("errors"));

        verify(this.productRestClient).createProduct("   ", null);
        verifyNoMoreInteractions(this.productRestClient);
    }
  
}