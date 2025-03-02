package bakery.mapper;

import bakery.dto.ClientDto;
import bakery.entity.Client;

public class ClientMapper implements Mapper<Client, ClientDto> {

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
