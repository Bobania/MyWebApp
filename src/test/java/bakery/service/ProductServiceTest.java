package bakery.service;

import bakery.dto.ProductDto;
import bakery.entity.Product;
import bakery.mapper.ProductMapper;
import bakery.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setup() {
        product = new Product(1L, "Bread", 1.99);
        productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setTitle("Bread");
        productDto.setPrice(1.99);
    }

    @Test
    void testCreateProduct() {
        ProductDto inputDto = new ProductDto();
        inputDto.setTitle("Bread");
        inputDto.setPrice(1.99);

        Product productEntity = new Product();
        productEntity.setId(null);
        productEntity.setTitle("Bread");
        productEntity.setPrice(1.99);

        Product createdEntity = new Product();
        createdEntity.setId(1L);
        createdEntity.setTitle("Bread");
        createdEntity.setPrice(1.99);

        when(productMapper.toEntity(inputDto)).thenReturn(productEntity);
        doNothing().when(productRepository).save(productEntity);
        when(productMapper.toDto(any(Product.class))).thenReturn(productDto);

        ProductDto result = productService.createProduct(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Bread", result.getTitle());
        assertEquals(1.99, result.getPrice());

        verify(productMapper).toEntity(inputDto);
        verify(productRepository).save(productEntity);
        verify(productMapper).toDto(any(Product.class));
    }

    @Test
    void testGetProductById() {
        doReturn(product).when(productRepository).findById(1L);
        doReturn(productDto).when(productMapper).toDto(product);
        ProductDto foundProductDto = productService.getProductById(1L);
        assertNotNull(foundProductDto);
        assertEquals(productDto, foundProductDto);
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetProductByIdNotFound() {
        doReturn(null).when(productRepository).findById(1L);

        ProductDto foundProductDto = productService.getProductById(1L);

        assertNull(foundProductDto);
        verify(productRepository).findById(1L);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(product);
        doReturn(products).when(productRepository).findAll();
        doReturn(productDto).when(productMapper).toDto(product);

        List<ProductDto> productDtos = productService.getAllProducts();

        assertNotNull(productDtos);
        assertEquals(1, productDtos.size());
        assertEquals(productDto, productDtos.get(0));
        verify(productRepository).findAll();
    }

    @Test
    void testUpdateProduct() {
        doReturn(product).when(productMapper).toEntity(productDto);

        productService.updateProduct(productDto);

        verify(productRepository).update(product);
    }

    @Test
    void testDeleteProduct() {
        doReturn(product).when(productRepository).findById(1L);
        doReturn(productDto).when(productMapper).toDto(product);

        ProductDto deletedProductDto = productService.deleteProduct(1L);

        assertNotNull(deletedProductDto);
        assertEquals(productDto, deletedProductDto);
        verify(productRepository).delete(1L);
    }

    @Test
    void testDeleteProductNotFound() {
        doReturn(null).when(productRepository).findById(1L);

        ProductDto deletedProductDto = productService.deleteProduct(1L);
        assertNull(deletedProductDto);
        verify(productRepository).findById(1L);
    }
}