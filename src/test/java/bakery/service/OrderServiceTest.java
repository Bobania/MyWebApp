package bakery.service;

import bakery.dto.OrderDto;
import bakery.entity.Order;
import bakery.mapper.OrderMapper;
import bakery.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDto orderDto;

    @BeforeEach
    void setup() {
        order = new Order();
        order.setId(1L);


        orderDto = new OrderDto();
        orderDto.setId(1L);

    }

    @Test
    void testCreateOrder() {
        System.out.println("Running testCreateOrder...");
        OrderDto inputDto = new OrderDto();
        inputDto.setClientId(1L);
        Order orderEntity = new Order();
        orderEntity.setId(null);

        when(orderMapper.toEntity(inputDto)).thenReturn(orderEntity);

        doAnswer(invocation -> {
            orderEntity.setId(1L);
            return null;
        }).when(orderRepository).save(orderEntity);

        when(orderMapper.toDto(orderEntity)).thenAnswer(invocation -> {
            OrderDto dto = new OrderDto();
            dto.setId(1L);
            dto.setClientId(1L);
            return dto;
        });

        doNothing().when(orderRepository).addClientToOrder(1L, 1L);

        OrderDto result = orderService.createOrder(inputDto);

        assertNotNull(result, "Result should not be null");
        assertEquals(1L, result.getId(), "Order ID should match");
        assertEquals(1L, result.getClientId(), "Client ID should match");

        verify(orderMapper).toEntity(inputDto);
        verify(orderRepository).save(orderEntity);
        verify(orderRepository).addClientToOrder(1L, 1L);
        verify(orderMapper).toDto(orderEntity);
        System.out.println("testCreateOrder passed.");
    }
    @Test
    void testGetOrderById() {
        doReturn(order).when(orderRepository).findById(1L);
        doReturn(orderDto).when(orderMapper).toDto(order);
        OrderDto foundOrderDto = orderService.getOrderById(1L);
        assertNotNull(foundOrderDto);
        assertEquals(orderDto, foundOrderDto);
        verify(orderRepository).findById(1L);
    }

    @Test
    void testGetOrderByIdNotFound() {
        doReturn(null).when(orderRepository).findById(1L);

        OrderDto foundOrderDto = orderService.getOrderById(1L);

        assertNull(foundOrderDto);
        verify(orderRepository).findById(1L);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        doReturn(orders).when(orderRepository).findAll();
        doReturn(orderDto).when(orderMapper).toDto(order);

        List<OrderDto> orderDtos = orderService.getAllOrders();

        assertNotNull(orderDtos);
        assertEquals(1, orderDtos.size());
        assertEquals(orderDto, orderDtos.get(0));
        verify(orderRepository).findAll();
    }

    @Test
    void testUpdateOrder() {
        doReturn(order).when(orderMapper).toEntity(orderDto);

        orderService.updateOrder(orderDto);

        verify(orderRepository).update(order);
    }

    @Test
    void testDeleteOrder() {
        doReturn(order).when(orderRepository).findById(1L);
        doReturn(orderDto).when(orderMapper).toDto(order);

        OrderDto deletedOrderDto = orderService.deleteOrder(1L);

        assertNotNull(deletedOrderDto);
        assertEquals(orderDto, deletedOrderDto);
        verify(orderRepository).delete(1L);
    }

    @Test
    void testDeleteOrderNotFound() {
        doReturn(null).when(orderRepository).findById(1L);

        OrderDto deletedOrderDto = orderService.deleteOrder(1L);
        assertNull(deletedOrderDto);
        verify(orderRepository).findById(1L);
    }
}