package by.clevertec.proxy.service.impl;

import static by.clevertec.proxy.util.TestConstant.NEW_PRODUCT_DESCRIPTION;
import static by.clevertec.proxy.util.TestConstant.NEW_PRODUCT_NAME;
import static by.clevertec.proxy.util.TestConstant.NEW_PRODUCT_PRICE;
import static by.clevertec.proxy.util.TestConstant.PRODUCT_INCORRECT_UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.proxy.cache.Cache;
import by.clevertec.proxy.data.InfoProductDto;
import by.clevertec.proxy.data.ProductDto;
import by.clevertec.proxy.entity.Product;
import by.clevertec.proxy.exception.ProductNotFoundException;
import by.clevertec.proxy.mapper.ProductMapper;
import by.clevertec.proxy.proxy.CacheableAspect;
import by.clevertec.proxy.repository.ProductRepository;
import by.clevertec.proxy.util.ProductTestBuilder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductMapper mapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private Cache<UUID, InfoProductDto> cache;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    @InjectMocks
    private CacheableAspect cacheableAspect;

    @InjectMocks
    private ProductServiceImpl productService;

    @Captor
    private ArgumentCaptor<Product> captor;

    @Nested
    class GetTest {

        @Test
        void getShouldReturnInfoProductDto_whenCorrectUuid() {
            InfoProductDto expected = ProductTestBuilder.builder()
                    .build()
                    .buildInfoProductDto();
            Product product = ProductTestBuilder.builder()
                    .build()
                    .buildProduct();
            Optional<Product> optionalProduct = Optional.of(product);

            when(joinPoint.getSignature())
                    .thenReturn(signature);
            when(signature.getName())
                    .thenReturn("get");
            when(joinPoint.getArgs())
                    .thenReturn(new Object[]{product.getUuid()});
            when(cache.get(product.getUuid()))
                    .thenReturn(null);
            when(productRepository.findById(any(UUID.class)))
                    .thenReturn(optionalProduct);
            when(mapper.toInfoProductDto(optionalProduct.get()))
                    .thenReturn(expected);
            doNothing()
                    .when(cache).put(product.getUuid(), expected);

            InfoProductDto actual = productService.get(product.getUuid());

            assertThat(actual)
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, expected.uuid())
                    .hasFieldOrPropertyWithValue(Product.Fields.name, expected.name())
                    .hasFieldOrPropertyWithValue(Product.Fields.description, expected.description())
                    .hasFieldOrPropertyWithValue(Product.Fields.price, expected.price());
        }

        @Test
        void getShouldReturnProductNotFoundException_whenIncorrectUuid() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(null)
                    .build()
                    .buildProduct();
            Optional<Product> empty = Optional.empty();

            when(productRepository.findById(product.getUuid()))
                    .thenReturn(empty);

            assertThrows(ProductNotFoundException.class, () -> productService.get(product.getUuid()));
        }
    }

    @Nested
    class GetAllTest {

        @Test
        void getAllShouldReturnInfoProductDtoList_whenProductsListIsNotEmpty() {
            InfoProductDto infoProductDto = ProductTestBuilder.builder()
                    .build()
                    .buildInfoProductDto();
            List<InfoProductDto> expected = List.of(infoProductDto);
            Product product = ProductTestBuilder.builder()
                    .build()
                    .buildProduct();
            List<Product> products = List.of(product);

            when(productRepository.findAll())
                    .thenReturn(products);
            when(mapper.toInfoProductDto(product))
                    .thenReturn(infoProductDto);

            List<InfoProductDto> actual = productService.getAll();

            assertThat(actual)
                    .hasSize(expected.size())
                    .isEqualTo(expected);
            verify(mapper, atLeastOnce()).toInfoProductDto(any(Product.class));
        }

        @Test
        void getAllShouldReturnEmptyList_whenProductsListIsEmpty() {
            List<InfoProductDto> expected = List.of();
            List<Product> products = List.of();

            when(productRepository.findAll())
                    .thenReturn(products);

            List<InfoProductDto> actual = productService.getAll();

            assertThat(actual)
                    .hasSize(expected.size())
                    .containsAll(expected);
            verify(mapper, never()).toInfoProductDto(any(Product.class));
        }
    }

    @Nested
    class CreateTest {

        @Test
        void createShouldReturnUuid_whenProductDtoIsCorrect() {
            ProductDto productDto = ProductTestBuilder.builder()
                    .build()
                    .buildProductDto();
            Product product = ProductTestBuilder.builder()
                    .withUuid(null).build()
                    .buildProduct();
            UUID generetedUuid = UUID.fromString("d3d75177-f087-4c70-ae30-05d947733c4e");
            Product createdProduct = ProductTestBuilder.builder()
                    .withUuid(generetedUuid).build()
                    .buildProduct();

            when(mapper.toProduct(productDto))
                    .thenReturn(product);
            when(productRepository.save(product))
                    .thenReturn(createdProduct);

            UUID actual = productService.create(productDto);

            assertEquals(createdProduct.getUuid(), actual);
        }

        @Test
        void createShouldSetProductUuid_whenInvokeRepository() {
            ProductDto productDto = ProductTestBuilder.builder()
                    .build()
                    .buildProductDto();
            Product productToSave = ProductTestBuilder.builder()
                    .withUuid(null).build()
                    .buildProduct();
            Product createdProduct = ProductTestBuilder.builder()
                    .build()
                    .buildProduct();

            when(mapper.toProduct(productDto))
                    .thenReturn(productToSave);
            when(productRepository.save(productToSave))
                    .thenReturn(createdProduct);

            productService.create(productDto);

            verify(productRepository, atLeastOnce()).save(captor.capture());
            assertThat(captor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, null);
        }

        @Test
        void createShouldLeaveProductUuid_whenProductDtoUuidExist() {
            ProductDto productDto = ProductTestBuilder.builder()
                    .build()
                    .buildProductDto();
            Product productToSave = ProductTestBuilder.builder()
                    .build()
                    .buildProduct();

            when(mapper.toProduct(productDto))
                    .thenReturn(productToSave);
            when(productRepository.save(productToSave))
                    .thenReturn(productToSave);

            productService.create(productDto);

            verify(productRepository, atLeastOnce()).save(captor.capture());
            assertThat(captor.getValue())
                    .hasFieldOrPropertyWithValue(Product.Fields.uuid, productToSave.getUuid());
        }

        @Test
        void createShouldReturnIllegalArgumentException_whenProductDtoIsNull() {
            ProductDto productDto = null;
            IllegalArgumentException exception = new IllegalArgumentException();

            when(productRepository.save(null))
                    .thenThrow(exception);

            assertThrows(IllegalArgumentException.class, () -> productService.create(productDto));
            verify(mapper, never()).toProduct(any(ProductDto.class));
            verify(productRepository, never()).save(any(Product.class));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void updateShouldUpdateProduct_whenProductDtoAndUuidIsCorrect() {
            Product product = ProductTestBuilder.builder().build().buildProduct();
            UUID uuid = product.getUuid();
            Optional<Product> optionalProduct = Optional.of(product);
            ProductDto updatedProductDto = ProductTestBuilder.builder()
                    .withName(NEW_PRODUCT_NAME)
                    .withDescription(NEW_PRODUCT_DESCRIPTION)
                    .withPrice(NEW_PRODUCT_PRICE).build()
                    .buildProductDto();
            Product updatedProduct = ProductTestBuilder.builder()
                    .withUuid(uuid)
                    .withName(NEW_PRODUCT_NAME)
                    .withDescription(NEW_PRODUCT_DESCRIPTION)
                    .withPrice(NEW_PRODUCT_PRICE).build()
                    .buildProduct();

            when(productRepository.findById(uuid))
                    .thenReturn(optionalProduct);
            when(mapper.merge(product, updatedProductDto))
                    .thenReturn(updatedProduct);
            when(productRepository.save(updatedProduct))
                    .thenReturn(updatedProduct);

            productService.update(uuid, updatedProductDto);
        }

        @Test
        void updateShouldReturnProductNotFoundException_whenIncorrectUuid() {
            Product product = ProductTestBuilder.builder()
                    .withUuid(PRODUCT_INCORRECT_UUID)
                    .build()
                    .buildProduct();
            ProductNotFoundException exception = new ProductNotFoundException(product.getUuid());

            when(productRepository.findById(product.getUuid()))
                    .thenThrow(exception);

            assertThrows(ProductNotFoundException.class, () -> productService.update(product.getUuid(), any(ProductDto.class)));
            verify(mapper, never()).toInfoProductDto(product);
            verify(productRepository, never()).save(any(Product.class));
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void deleteShouldDeleteProduct_whenCorrectUuid() {
            UUID uuid = ProductTestBuilder.builder().build().getUuid();

            doNothing()
                    .when(productRepository).delete(uuid);

            productService.delete(uuid);
        }
    }
}
