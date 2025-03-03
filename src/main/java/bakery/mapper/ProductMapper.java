package bakery.mapper;

import bakery.dto.ProductDto;
import bakery.entity.Product;

/**
 * Этот класс используется для конвертации объектов Product в объекты ProductDto и наоборот
 */

public class ProductMapper implements Mapper<Product, ProductDto> {

    /**
     * Преобразует сущность Product в объект DTO ProductDto
     *
     * @param product сущность Product, которую нужно преобразовать
     * @return объект ProductDto, представляющий сущность Product, или null, если входной параметр равен null
     */

    @Override
    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setPrice(product.getPrice());
        return productDto;
    }
    /**
     * Преобразует объект DTO ProductDto в сущность Product
     *
     * @param productDto объект ProductDto, который нужно преобразовать
     * @return сущность Product, представляющая объект ProductDto, или null, если входной параметр равен null
     */
   @Override
   public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productDto.getId());
        product.setTitle(productDto.getTitle());
        product.setPrice(productDto.getPrice());
        return product;
   }
}
