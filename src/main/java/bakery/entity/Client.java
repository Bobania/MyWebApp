package bakery.entity;
import lombok.*;

import java.util.Objects;


/**
 * Этот класс используется для хранения информации о клиенте, такой как идентификатор, имя, фамилия и номер телефона
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private Long id;
    private String name;
    private String surname;
    private String phone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client)) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(name, client.name) &&
                Objects.equals(surname, client.surname) &&
                Objects.equals(phone, client.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, phone);
    }
}

