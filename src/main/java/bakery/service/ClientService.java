package bakery.service;

import bakery.dto.ClientDto;
import bakery.entity.Client;
import bakery.mapper.ClientMapper;
import bakery.repository.ClientRepository;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления клиентами, предоставляющий методы для создания, получения, обновления и удаления клиентов
 */
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    /**
     * Конструктор для инициализации ClientService с репозиторием и маппером клиентов
     *
     * @param clientRepository репозиторий для работы с сущностями Client
     * @param clientMapper маппер для преобразования между Client и ClientDto
     */
    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    /**
     * Создает нового клиента на основе переданного ClientDto
     *
     * @param clientDto DTO клиента для создания
     * @return созданный ClientDto
     */
    public ClientDto createClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    /**
     * Находит клиента по его id
     *
     * @param id id клиента для поиска
     * @return ClientDto с указанным id, или null, если клиент не найден
     */
    public ClientDto getClientById(Long id) {
        Client client = clientRepository.findById(id);
        return clientMapper.toDto(client);
    }

    /**
     * Получает список всех клиентов
     *
     * @return список всех клиентов в виде ClientDto
     */
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет существующего клиента на основе переданного ClientDto
     *
     * @param clientDto DTO клиента для обновления
     */
    public void updateClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        clientRepository.update(client);
    }

    /**
     * Удаляет клиента по его id
     *
     * @param id id клиента для удаления
     * @return ClientDto удаленного клиента, или null, если клиент не найден
     */
    public ClientDto deleteClient(Long id) {
        Client client = clientRepository.findById(id);
        if (client != null) {
            clientRepository.delete(id);
            return clientMapper.toDto(client);
        }
        return null;
    }
}
