package bakery.service;

import bakery.dto.ProductDto;
import bakery.entity.Product;
import bakery.mapper.ProductMapper;
import bakery.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления продуктами, предоставляющий методы для создания, получения, обновления и удаления продуктов
 */
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;



    /**
     * Конструктор для инициализации ProductService с репозиторием и маппером продуктов
     *
     * @param productRepository репозиторий для работы с сущностями Product
     * @param productMapper маппер для преобразования между Product и ProductDto
     */
    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Создает новый продукт на основе переданного ProductDto
     *
     * @param productDto DTO продукта для создания
     * @return созданный ProductDto
     */
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Находит продукт по его id
     *
     * @param id id продукта для поиска
     * @return ProductDto с указанным id, или null, если продукт не найден
     */
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id);
        return productMapper.toDto(product);
    }

    /**
     * Получает список всех продуктов
     *
     * @return список всех продуктов в виде ProductDto
     */
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет существующий продукт на основе переданного ProductDto
     *
     * @param productDto DTO продукта для обновления
     */
    public void updateProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        productRepository.update(product);
    }

    /**
     * Удаляет продукт по его id
     *
     * @param id id продукта для удаления
     * @return ProductDto удаленного продукта, или null, если продукт не найден
     */
    public ProductDto deleteProduct(Long id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            productRepository.delete(id);
            return productMapper.toDto(product);
        }
        return null;
    }

}
