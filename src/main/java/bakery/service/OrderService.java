package bakery.service;

import bakery.dto.OrderDto;
import bakery.entity.Order;
import bakery.mapper.OrderMapper;
import bakery.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления заказами, предоставляющий методы для создания, получения, обновления и удаления заказов
 */
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    /**
     * Конструктор для инициализации OrderService с репозиторием и маппером заказов
     *
     * @param orderRepository репозиторий для работы с сущностями Order
     * @param orderMapper маппер для преобразования между Order и OrderDto
     */
    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    /**
     * Создает новый заказ на основе переданного OrderDto
     *
     * @param orderDto DTO заказа для создания
     * @return созданный OrderDto
     */
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        orderRepository.save(order);

        // Добавить клиентов к заказу
        if (orderDto.getClientId() != null) {
            orderRepository.addClientToOrder(order.getId(), orderDto.getClientId());
        }

        return orderMapper.toDto(order);
    }

    /**
     * Находит заказ по его id
     *
     * @param id id заказа для поиска
     * @return OrderDto с указанным id, или null, если заказ не найден
     */
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        return orderMapper.toDto(order);
    }

    /**
     * Получает список всех заказов
     *
     * @return список всех заказов в виде OrderDto
     */
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Обновляет существующий заказ на основе переданного OrderDto
     *
     * @param orderDto DTO заказа для обновления
     */
    public void updateOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        orderRepository.update(order);
    }

    /**
     * Удаляет заказ по его id
     *
     * @param id id заказа для удаления
     * @return OrderDto удаленного заказа, или null, если заказ не найден
     */
    public OrderDto deleteOrder(Long id) {
        Order order = orderRepository.findById(id);
        if (order != null) {
            orderRepository.delete(id);
            return orderMapper.toDto(order);
        }
        return null;
    }
}
