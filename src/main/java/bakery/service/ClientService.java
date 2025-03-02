package bakery.service;

import bakery.dto.ClientDto;
import bakery.entity.Client;
import bakery.mapper.ClientMapper;
import bakery.repository.ClientRepository;


import java.util.List;
import java.util.stream.Collectors;

public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public ClientDto createClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id);
        return clientMapper.toDto(client);
    }

    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    public void updateClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        clientRepository.update(client);
    }

    public ClientDto deleteClient(Long id) {
        Client client = clientRepository.findById(id);
        if (client != null) {
            clientRepository.delete(id);
            return clientMapper.toDto(client);
        }
        return null;
    }
}
