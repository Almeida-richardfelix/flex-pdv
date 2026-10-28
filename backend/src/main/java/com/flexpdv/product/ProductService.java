package com.flexpdv.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsByCode(request.code())) {
            throw new ResponseStatusException(CONFLICT, "Já existe um produto com esse código");
        }

        Product product = new Product(
                request.code(),
                request.name(),
                request.price(),
                request.stock()
        );
        return ProductResponse.from(productRepository.save(product));
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(long id) {
        return productRepository.findById(id)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }

    @Transactional(readOnly = true)
    public ProductResponse findByCode(String code) {
        return productRepository.findByCode(code)
                .map(ProductResponse::from)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado"));
    }
}
