package bakery.repository;

import bakery.entity.Client;
import bakery.util.DatabaseUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClientRepositoryTest {

    private ClientRepository clientRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MockedStatic<DatabaseUtil> databaseUtilMock;

    @BeforeEach
    void setUp() throws SQLException {
        clientRepository = new ClientRepository();
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // Мокаем DatabaseUtil
        databaseUtilMock = Mockito.mockStatic(DatabaseUtil.class);
        databaseUtilMock.when(DatabaseUtil::getConnection).thenReturn(connection);
    }
    @AfterEach
    void tearDown() {
        // Закрываем статический мок после каждого теста
        databaseUtilMock.close();
    }

    @Test
    void testSave() throws SQLException {
        Client client = new Client(1L, "John", "Doe", "1234567890");

        when(connection.prepareStatement(any(String.class), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        clientRepository.save(client);

        assertNotNull(client.getId());
        assertEquals(1L, client.getId());
        verify(preparedStatement).setString(1, "John");
        verify(preparedStatement).setString(2, "Doe");
        verify(preparedStatement).setString(3, "1234567890");
    }

    @Test
    void testFindById() throws SQLException {
        Client expectedClient = new Client(1L, "John", "Doe", "1234567890");

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(expectedClient.getId());
        when(resultSet.getString("name")).thenReturn(expectedClient.getName());
        when(resultSet.getString("surname")).thenReturn(expectedClient.getSurname());
        when(resultSet.getString("phone")).thenReturn(expectedClient.getPhone());

        Client actualClient = clientRepository.findById(1L);

        assertNotNull(actualClient);
        assertEquals(expectedClient, actualClient);
        verify(preparedStatement).setLong(1, 1L);
    }


    @Test
    void testFindAll() throws SQLException {
        Client client1 = new Client(1L, "John", "Doe", "1234567890");
        Client client2 = new Client(2L, "Jane", "Doe", "0987654321");

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(client1.getId()).thenReturn(client2.getId());
        when(resultSet.getString("name")).thenReturn(client1.getName()).thenReturn(client2.getName());
        when(resultSet.getString("surname")).thenReturn(client1.getSurname()).thenReturn(client2.getSurname());
        when(resultSet.getString("phone")).thenReturn(client1.getPhone()).thenReturn(client2.getPhone());

        List<Client> clients = clientRepository.findAll();

        assertNotNull(clients);
        assertEquals(2, clients.size());
        assertEquals(client1, clients.get(0));
        assertEquals(client2, clients.get(1));
    }

    @Test
    void testUpdate() throws SQLException {
        Client client = new Client(1L, "John", "Doe", "1234567890");

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        clientRepository.update(client);

        verify(preparedStatement).setString(1, "John");
        verify(preparedStatement).setString(2, "Doe");
        verify(preparedStatement).setString(3, "1234567890");
        verify(preparedStatement).setLong(4, 1L);
    }

    @Test
    void testDelete() throws SQLException {
        Long clientId = 1L;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        clientRepository.delete(clientId);

        verify(preparedStatement).setLong(1, clientId);
    }
}