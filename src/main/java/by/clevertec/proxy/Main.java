package by.clevertec.proxy;

import by.clevertec.proxy.data.InfoProductDto;
import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.repository.impl.ProductRepositoryImpl;
import by.clevertec.proxy.repository.util.DataSource;
import by.clevertec.proxy.service.ProductService;
import by.clevertec.proxy.service.impl.ProductServiceImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("REP");
        ProductRepository productRepository = new ProductRepositoryImpl();
        List<Product> products = productRepository.findAll();
        System.out.println(products);
        System.out.println("SERV");
        ProductService productService = new ProductServiceImpl();
        List<InfoProductDto> infoProducts = productService.getAll();
        System.out.println(infoProducts);
        DataSource.closeDataSource();
    }
}