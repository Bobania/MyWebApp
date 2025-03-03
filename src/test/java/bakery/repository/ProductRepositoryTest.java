package bakery.repository;

import bakery.entity.Product;
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

public class ProductRepositoryTest {

    private ProductRepository productRepository;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private MockedStatic<DatabaseUtil> databaseUtilMock;

    @BeforeEach
    void setUp() throws SQLException {
        productRepository = new ProductRepository();
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
        Product product = new Product(null, "Bread", 1.99);

        when(connection.prepareStatement(any(String.class), anyInt())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong(1)).thenReturn(1L);

        productRepository.save(product);

        assertNotNull(product.getId());
        assertEquals(1L, product.getId());
        verify(preparedStatement).setString(1, "Bread");
        verify(preparedStatement).setDouble(2, 1.99);
    }

    @Test
    void testFindById() throws SQLException {
        Product expectedProduct = new Product(1L, "Bread", 1.99);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getLong("id")).thenReturn(expectedProduct.getId());
        when(resultSet.getString("title")).thenReturn(expectedProduct.getTitle());
        when(resultSet.getDouble("price")).thenReturn(expectedProduct.getPrice());

        Product actualProduct = productRepository.findById(1L);

        assertNotNull(actualProduct);
        assertEquals(expectedProduct, actualProduct);
        verify(preparedStatement).setLong(1, 1L);
    }

    @Test
    void testFindAll() throws SQLException {
        Product product1 = new Product(1L, "Bread", 1.99);
        Product product2 = new Product(2L, "Cake", 2.99);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong("id")).thenReturn(product1.getId()).thenReturn(product2.getId());
        when(resultSet.getString("title")).thenReturn(product1.getTitle()).thenReturn(product2.getTitle());
        when(resultSet.getDouble("price")).thenReturn(product1.getPrice()).thenReturn(product2.getPrice());

        List<Product> products = productRepository.findAll();

        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(product1, products.get(0));
        assertEquals(product2, products.get(1));
    }

    @Test
    void testUpdate() throws SQLException {
        Product product = new Product(1L, "Bread", 1.99);

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        productRepository.update(product);

        verify(preparedStatement).setString(1, "Bread");
        verify(preparedStatement).setDouble(2, 1.99);
        verify(preparedStatement).setLong(3, 1L);
    }
    @Test
    void testDelete() throws SQLException {
        Long productId = 1L;

        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        productRepository.delete(productId);

        verify(preparedStatement).setLong(1, productId);
        verify(preparedStatement).executeUpdate();
    }
}