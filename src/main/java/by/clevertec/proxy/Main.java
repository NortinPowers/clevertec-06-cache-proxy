package by.clevertec.proxy;

import by.clevertec.proxy.data.ProductDto;
import by.clevertec.proxy.repository.util.DataSource;
import by.clevertec.proxy.service.ProductService;
import by.clevertec.proxy.service.impl.ProductServiceImpl;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        ProductService productService = new ProductServiceImpl();

//        InfoProductDto infoProductDto = productService.get(UUID.fromString("021652ee-06bb-4bc6-a70c-2b99bee7eca3"));
//        System.out.println(infoProductDto);
//
//        productService.create(new ProductDto("Продукт", "Описание продукта", BigDecimal.TEN));

        productService.update(UUID.fromString("e74d0cd0-4573-42a4-a74e-8d81ad7b4923"), new ProductDto("продукт", "описание продукта", null));

        DataSource.closeDataSource();
    }
}
