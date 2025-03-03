package bakery.repository;

import bakery.entity.Client;
import bakery.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий для управления сущностями Client в базе данных
 */
public class ClientRepository {
    private static final String SQL_INSERT_CLIENT = "INSERT INTO clients (name, surname, phone) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_CLIENT_BY_ID = "SELECT * FROM clients WHERE id = ?";
    private static final String SQL_SELECT_ALL_CLIENTS = "SELECT * FROM clients";
    private static final String SQL_UPDATE_CLIENT = "UPDATE clients SET name = ?, surname = ?, phone = ? WHERE id = ?";
    private static final String SQL_DELETE_CLIENT = "DELETE FROM clients WHERE id = ?";

    /**
     * Сохраняет новую сущность Client в базе данных
     *
     * @param client сущность Client для сохранения
     */
    public void save(Client client) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_CLIENT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setString(3, client.getPhone());

            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                client.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * Находит сущность Client по её id
     *
     * @param id, id сущности Client для поиска
     * @return сущность Client с указанным id, или null, если не найдена
     */
    public Client findById(Long id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_CLIENT_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Находит все сущности Client в базе данных
     *
     * @return список всех сущностей Client
     */
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_CLIENTS);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                clients.add(new Client(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    /**
     * Обновляет существующую сущность Client в базе данных
     *
     * @param client сущность Client для обновления
     */
    public void update(Client client) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_CLIENT)) {
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getSurname());
            preparedStatement.setString(3, client.getPhone());
            preparedStatement.setLong(4, client.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаляет сущность Client из базы данных по id
     *
     * @param id, id сущности Client для удаления
     */
    public void delete(Long id) {
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_CLIENT)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

