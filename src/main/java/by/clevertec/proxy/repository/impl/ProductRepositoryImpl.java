package by.clevertec.proxy.repository.impl;

import static by.clevertec.proxy.repository.util.DataSource.getDataSource;
import static by.clevertec.proxy.utils.LogUtil.getErrorMessageToLog;

import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.repository.util.LocalDateTimeProcessor;
import by.clevertec.proxy.repository.util.LocalDateTimeRowProcessor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

@Log4j2
public class ProductRepositoryImpl implements ProductRepository {

    private static final String GET_ALL_PRODUCT = "select * from products";
    private final QueryRunner queryRunner;
    private final LocalDateTimeRowProcessor rowProcessor;

    {
        BasicDataSource dataSource = getDataSource();
        LocalDateTimeProcessor columnProcessor = new LocalDateTimeProcessor();
        rowProcessor = new LocalDateTimeRowProcessor(columnProcessor);
        queryRunner = new QueryRunner(dataSource);
    }

    @Override
    public Optional<Product> findById(UUID uuid) {
        return  null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {
            products = queryRunner.query(GET_ALL_PRODUCT, new BeanListHandler<>(Product.class, rowProcessor));
        } catch (Exception exception){
            log.error(getErrorMessageToLog("findAll()", ProductRepositoryImpl.class), exception);
        }
        return products;
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }
}
