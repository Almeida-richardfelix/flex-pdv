package com.flexpdv.product;

import java.math.BigDecimal;

public record ProductResponse(Long id, String code, String name, BigDecimal price, int stock) {

    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPrice(),
                product.getStock()
        );
    }
}
