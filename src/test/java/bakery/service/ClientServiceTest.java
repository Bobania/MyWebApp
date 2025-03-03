package bakery.service;

import bakery.dto.ClientDto;
import bakery.entity.Client;
import bakery.mapper.ClientMapper;
import bakery.repository.ClientRepository;
import bakery.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private Client client;
    private ClientDto clientDto;

    @BeforeEach
    void setup() {
        client = new Client(1L, "John", "Doe", "1234567890");
        clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName("John");
        clientDto.setSurname("Doe");
        clientDto.setPhone("1234567890");
    }

    @Test
    void testCreateClient() {
        ClientDto inputDto = new ClientDto();
        inputDto.setId(1L);
        inputDto.setName("John");
        inputDto.setSurname("Doe");
        inputDto.setPhone("1234567890");

        Client clientEntity = new Client();
        clientEntity.setId(null);
        clientEntity.setName("John");
        clientEntity.setSurname("Doe");
        clientEntity.setPhone("1234567890");

        Client createdEntity = new Client();
        createdEntity.setId(1L);
        createdEntity.setName("John");
        createdEntity.setSurname("Doe");
        createdEntity.setPhone("1234567890");

        ClientDto outputDto = new ClientDto();
        outputDto.setId(1L);
        outputDto.setName("John");
        outputDto.setSurname("Doe");
        outputDto.setPhone("1234567890");

        when(clientMapper.toEntity(inputDto)).thenReturn(clientEntity);
        doNothing().when(clientRepository).save(clientEntity);
        when(clientMapper.toDto(any(Client.class))).thenReturn(outputDto);

        ClientDto result = clientService.createClient(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());
        assertEquals("Doe", result.getSurname());
        assertEquals("1234567890", result.getPhone());


        verify(clientMapper).toEntity(inputDto);
        verify(clientRepository).save(clientEntity);
        verify(clientMapper).toDto(any(Client.class)); // Измените здесь

    }
    @Test
    void testGetClientById() {
        doReturn(client).when(clientRepository).findById(1L);
        doReturn(clientDto).when(clientMapper).toDto(client);
        ClientDto foundClientDto = clientService.getClientById(1L);
        assertNotNull(foundClientDto);
        assertEquals(clientDto, foundClientDto);
        verify(clientRepository).findById(1L);
    }

    @Test
    void testGetClientByIdNotFound() {

        doReturn(null).when(clientRepository).findById(1L);

        ClientDto foundClientDto = clientService.getClientById(1L);

        assertNull(foundClientDto);
        verify(clientRepository).findById(1L);
    }

    @Test
    void testGetAllClients() {

        List<Client> clients = new ArrayList<>();
        clients.add(client);
        doReturn(clients).when(clientRepository).findAll();
        doReturn(clientDto).when(clientMapper).toDto(client);

        List<ClientDto> clientDtos = clientService.getAllClients();

        assertNotNull(clientDtos);
        assertEquals(1, clientDtos.size());
        assertEquals(clientDto, clientDtos.get(0));
        verify(clientRepository).findAll();
    }

    @Test
    void testUpdateClient() {

        doReturn(client).when(clientMapper).toEntity(clientDto);

        clientService.updateClient(clientDto);

        verify(clientRepository).update(client);
    }

    @Test
    void testDeleteClient() {
        // Arrange
        doReturn(client).when(clientRepository).findById(1L);
        doReturn(clientDto).when(clientMapper).toDto(client);

        ClientDto deletedClientDto = clientService.deleteClient(1L);

        assertNotNull(deletedClientDto);
        assertEquals(clientDto, deletedClientDto);
        verify(clientRepository).delete(1L);
    }

    @Test
    void testDeleteClientNotFound() {

        doReturn(null).when(clientRepository).findById(1L);

        ClientDto deletedClientDto = clientService.deleteClient(1L);
        assertNull(deletedClientDto);
        verify(clientRepository).findById(1L);
    }
}