package bakery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Этот класс используется для передачи данных клиента
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {
    private Long id;
    private String name;
    private String surname;
    private String phone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDto clientDto = (ClientDto) o;
        return Objects.equals(id, clientDto.id) &&
                Objects.equals(name, clientDto.name) &&
                Objects.equals(surname, clientDto.surname) &&
                Objects.equals(phone, clientDto.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, phone);
    }
}
