package bakery.service;

import bakery.dto.OrderDto;
import bakery.entity.Order;
import bakery.mapper.OrderMapper;
import bakery.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderDto createOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        orderRepository.save(order);

        // Добавить клиентов к заказу
        if (orderDto.getClientId() != null) {
            orderRepository.addClientToOrder(order.getId(), orderDto.getClientId());
        }

        return orderMapper.toDto(order);
    }
    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id);
        return orderMapper.toDto(order);
    }
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
    public void updateOrder(OrderDto orderDto) {
        Order order = orderMapper.toEntity(orderDto);
        orderRepository.update(order);
    }

    public OrderDto deleteOrder(Long id) {
        Order order = orderRepository.findById(id);
        if (order != null) {
            orderRepository.delete(id);
            return orderMapper.toDto(order);
        }
        return null;
    }


}
