package bakery.repository;

import bakery.entity.Product;
import bakery.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления сущностями Product в базе данных
 */
public class ProductRepository {
    private static final String SQL_INSERT_PRODUCT = "INSERT INTO products (title, price) VALUES (?, ?)";
    private static final String SQL_SELECT_PRODUCT_BY_ID = "SELECT * FROM products WHERE id = ?";
    private static final String SQL_SELECT_ALL_PRODUCTS = "SELECT * FROM products";
    private static final String SQL_UPDATE_PRODUCT = "UPDATE products SET title = ?, price = ? WHERE id = ?";
    private static final String SQL_DELETE_PRODUCT = "DELETE FROM products WHERE id = ?";

    /**
     * Сохраняет новую сущность Product в базе данных
     *
     * @param product сущность Product для сохранения
     */
    public void save(Product product) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, product.getTitle());
            preparedStatement.setDouble(2, product.getPrice());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                product.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Находит сущность Product по её id
     *
     * @param id, id сущности Product для поиска
     * @return сущность Product с указанным id, или null, если не найдена
     */
    public Product findById(Long id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_PRODUCT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getDouble("price")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Находит все сущности Product в базе данных
     *
     * @return список всех сущностей Product
     */
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_PRODUCTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getLong("id"),
                        resultSet.getString("title"),
                        resultSet.getDouble("price")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * Обновляет существующую сущность Product в базе данных
     *
     * @param product сущность Product для обновления
     */
    public void update(Product product) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_PRODUCT)) {
            preparedStatement.setString(1, product.getTitle());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setLong(3, product.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет сущность Product из базы данных по id
     *
     * @param id id сущности Product для удаления
     */
    public void delete(Long id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_PRODUCT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

