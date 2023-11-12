package by.clevertec.proxy.service.impl;

import by.clevertec.proxy.data.InfoProductDto;
import by.clevertec.proxy.data.ProductDto;
import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.exception.ProductNotFoundException;
import by.clevertec.proxy.mapper.ProductMapper;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.service.ProductService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper mapper;
    private final ProductRepository productRepository;

//    private final ProductValidator productValidator = new ProductValidator();

    @Override
    public InfoProductDto get(UUID uuid) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        Product product = productOptional.orElseThrow(() -> new ProductNotFoundException(uuid));
        return mapper.toInfoProductDto(product);
    }

    @Override
    public List<InfoProductDto> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(mapper::toInfoProductDto)
                .toList();
    }

    @Override
    public UUID create(ProductDto productDto) {
        Product product = mapper.toProduct(productDto);
        Product saved = productRepository.save(product);
        return saved.getUuid();
    }

    @Override
    public void update(UUID uuid, ProductDto productDto) {
        Optional<Product> productOptional = productRepository.findById(uuid);
        productOptional.map(product -> {
            Product updatedProduct = mapper.merge(product, productDto);
            productRepository.save(updatedProduct);
            return product;
        }).orElseThrow(() -> new ProductNotFoundException(uuid));
    }

    @Override
    public void delete(UUID uuid) {
        productRepository.delete(uuid);
    }
}
