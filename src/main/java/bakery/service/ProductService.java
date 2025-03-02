package bakery.service;

import bakery.dto.ProductDto;
import bakery.entity.Product;
import bakery.mapper.ProductMapper;
import bakery.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id);
        return productMapper.toDto(product);
    }

    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.update(product);
    }

    public ProductDto deleteProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            productRepository.delete(id);
            return productMapper.toDto(product);
        }
        return null;
    }

}
