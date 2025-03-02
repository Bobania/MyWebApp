package bakery.mapper;

import bakery.dto.OrderDto;
import bakery.entity.Order;
import bakery.repository.OrderRepository;

import java.util.List;

public class OrderMapper implements Mapper<Order, OrderDto> {
    private final OrderRepository orderRepository;

    public OrderMapper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();
        orderDto.setId(order.getId());
        List<Long> clientIds = orderRepository.getClientIdsByOrderId(order.getId());
        orderDto.setClientId(clientIds.isEmpty() ? null : clientIds.get(0));
        List<Long> productIds = orderRepository.getProductIdsByOrderId(order.getId());
        orderDto.setProductsId(productIds);

        return orderDto;
    }

    @Override
    public Order toEntity(OrderDto orderDto) {
        if (orderDto == null) {
            return null;
        }
        Order order = new Order();
        order.setId(orderDto.getId());

        return order;
    }
}
