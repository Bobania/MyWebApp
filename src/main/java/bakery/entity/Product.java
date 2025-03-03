package bakery.entity;
import lombok.*;

import java.util.Objects;

/**
 * Этот класс используется для хранения информации о продукте, такой как идентификатор, название и цена
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private Long id;
    private String title;
    private double price;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return Double.compare(product.price, price) == 0 &&
                Objects.equals(id, product.id) &&
                Objects.equals(title, product.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price);
    }
}
