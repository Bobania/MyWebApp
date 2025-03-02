package bakery.repository;

import bakery.entity.Order;
import bakery.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderRepository {
    private static final String SQL_INSERT_ORDER = "INSERT INTO orders (client_id) VALUES (?)";
    private static final String SQL_SELECT_ORDER_BY_ID = "SELECT * FROM orders WHERE id = ?";
    private static final String SQL_SELECT_ALL_ORDERS = "SELECT * FROM orders";
    private static final String SQL_UPDATE_ORDER = "UPDATE orders SET client_id = ? WHERE id = ?";
    private static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = ?";

    public void save(Order order) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO orders (id) VALUES (DEFAULT)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                order.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Order findById(Long id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ORDER_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Order(
                        resultSet.getLong("id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_ORDERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                orders.add(new Order(
                        resultSet.getLong("id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void update(Order order) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDER)) {
            preparedStatement.setLong(1, order.getId()); // Предполагается, что у вас есть clientId в Order
            preparedStatement.setLong(2, order.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Long id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_ORDER)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Long> getClientIdsByOrderId(Long orderId) {
        List<Long> clientIds = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT client_id FROM order_clients WHERE order_id = ?")) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                clientIds.add(resultSet.getLong("client_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientIds;
    }
    public List<Long> getProductIdsByOrderId(Long orderId) {
        List<Long> productIds = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT product_id FROM orders WHERE id = ?")) {
            preparedStatement.setLong(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                productIds.add(resultSet.getLong("product_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productIds;
    }

    public void addClientToOrder(Long orderId, Long clientId) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO order_clients (order_id, client_id) VALUES (?, ?)")) {
            preparedStatement.setLong(1, orderId);
            preparedStatement.setLong(2, clientId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
