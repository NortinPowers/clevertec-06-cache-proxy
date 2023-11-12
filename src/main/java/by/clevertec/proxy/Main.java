package by.clevertec.proxy;

import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.repository.impl.ProductRepositoryImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProductRepository productRepository = new ProductRepositoryImpl();
        List<Product> products = productRepository.findAll();
        System.out.println(products);
    }
}