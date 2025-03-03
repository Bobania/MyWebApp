package bakery.repository;

import bakery.entity.Order;
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

public class OrderRepositoryTest {

    private OrderRepository orderRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MockedStatic<DatabaseUtil> databaseUtilMock;

    @BeforeEach
    void setUp() throws SQLException {
        orderRepository = new OrderRepository();
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);

        // Мокаем DatabaseUtil
        databaseUtilMock = Mockito.mockStatic(DatabaseUtil.class);
        databaseUtilMock.when(DatabaseUtil::getConnection).thenReturn(connection);
    }

    @AfterEach
    void tearDown() {
        databaseUtilMock.close();
    }

    @Test
    void testSave() throws SQLException {
        Order order = new Order(null);

        when(connection.prepareStatement(any(String.class), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        orderRepository.save(order);

        assertNotNull(order.getId());
        assertEquals(1L, order.getId());
    }

    @Test
    void testFindById() throws SQLException {
        Order expectedOrder = new Order(1L);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(expectedOrder.getId());

        Order actualOrder = orderRepository.findById(1L);

        assertNotNull(actualOrder);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testFindAll() throws SQLException {
        Order order1 = new Order(1L);
        Order order2 = new Order(2L);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(order1.getId()).thenReturn(order2.getId());

        List<Order> orders = orderRepository.findAll();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        assertEquals(order1, orders.get(0));
        assertEquals(order2, orders.get(1));
    }

    @Test
    void testUpdate() throws SQLException {
        Order order = new Order(1L);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        orderRepository.update(order);

        verify(preparedStatement).setLong(1, order.getId());
        verify(preparedStatement).setLong(2, order.getId());
    }

    @Test
    void testDelete() throws SQLException {
        Long orderId = 1L;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        orderRepository.delete(orderId);

        verify(preparedStatement).setLong(1, orderId);
    }
}