package bakery.mapper;

import bakery.dto.ClientDto;
import bakery.entity.Client;

/**
 * Этот класс используется для конвертации объектов Client в объекты ClientDto и наоборот
 */

public class ClientMapper implements Mapper<Client, ClientDto> {

    /**
     * Преобразует сущность Client в объект DTO ClientDto
     *
     * @param client сущность Client, которую нужно преобразовать
     * @return объект ClientDto, представляющий сущность Client, или null, если входной параметр равен null
     */
    @Override
    public  ClientDto toDto(Client client)  {
        if(client == null){
            return null;
        }
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setSurname(client.getSurname());
        clientDto.setPhone(client.getPhone());
        return clientDto;
    }
    /**
     * Преобразует объект DTO ClientDto в сущность Client
     *
     * @param clientDto объект ClientDto, который нужно преобразовать
     * @return сущность Client, представляющая объект ClientDto, или null, если входной параметр равен null
     */
    @Override
    public Client toEntity(ClientDto clientDto) {
        if(clientDto == null){
            return null;
        }
        Client client = new Client();
        client.setId(clientDto.getId());
        client.setName(clientDto.getName());
        client.setSurname(clientDto.getSurname());
        client.setPhone(clientDto.getPhone());
        return client;
    }


}
