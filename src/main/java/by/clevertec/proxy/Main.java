package by.clevertec.proxy;

import by.clevertec.proxy.mapper.ProductMapper;
import by.clevertec.proxy.mapper.ProductMapperImpl;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.repository.impl.ProductRepositoryImpl;
import by.clevertec.proxy.repository.util.DataSource;
import by.clevertec.proxy.service.ProductService;
import by.clevertec.proxy.service.impl.ProductServiceImpl;
import by.clevertec.proxy.validator.ProductDtoValidator;

public class Main {
    public static void main(String[] args) {
        ProductDtoValidator validator = new ProductDtoValidator();
        ProductRepository productRepository = new ProductRepositoryImpl();
        ProductMapper productMapper = new ProductMapperImpl();
        ProductService productService = new ProductServiceImpl(productMapper, productRepository, validator);

        DataSource.closeDataSource();
    }
}
