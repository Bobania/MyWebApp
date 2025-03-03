package bakery.dto;

import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Этот класс используется для передачи данных о заказе
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private Long clientId;
    private List<Long> productsId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto orderDto = (OrderDto) o;
        return Objects.equals(id, orderDto.id) &&
                Objects.equals(clientId, orderDto.clientId) &&
                Objects.equals(productsId, orderDto.productsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, productsId);
    }
}
