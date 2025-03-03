package bakery.servlets;

import bakery.dto.OrderDto;
import bakery.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderServletTest {

    @Mock
    private OrderService orderService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private OrderServlet orderServlet;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        orderServlet.orderService = orderService;
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDoGet_WithId() throws ServletException, IOException {
        Long orderId = 1L;
        OrderDto orderDto = new OrderDto(orderId, 1L, Arrays.asList(1L, 2L));

        when(request.getPathInfo()).thenReturn("/" + orderId);
        when(orderService.getOrderById(orderId)).thenReturn(orderDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(orderDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoGet_WithoutId() throws ServletException, IOException {
        List<OrderDto> orders = Arrays.asList(
                new OrderDto(1L, 1L, Arrays.asList(1L, 2L)),
                new OrderDto(2L, 2L, Arrays.asList(3L, 4L))
        );

        when(request.getPathInfo()).thenReturn(null);
        when(orderService.getAllOrders()).thenReturn(orders);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(orders);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        OrderDto orderDto = new OrderDto(null, 1L, Arrays.asList(1L, 2L));
        OrderDto createdOrderDto = new OrderDto(1L, 1L, Arrays.asList(1L, 2L));

        byte[] jsonBytes = objectMapper.writeValueAsBytes(orderDto);
        ServletInputStream servletInputStream = new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonBytes);

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Не используется в тесте
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        when(request.getInputStream()).thenReturn(servletInputStream);
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(createdOrderDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(createdOrderDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPut() throws ServletException, IOException {
        OrderDto orderDto = new OrderDto(1L, 1L, Arrays.asList(1L, 2L));

        byte[] jsonBytes = objectMapper.writeValueAsBytes(orderDto);
        ServletInputStream servletInputStream = new ServletInputStream() {
            private final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(jsonBytes);

            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
                // Не используется в тесте
            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };

        when(request.getInputStream()).thenReturn(servletInputStream);

        orderServlet.doPut(request, response);

        ArgumentCaptor<OrderDto> captor = ArgumentCaptor.forClass(OrderDto.class);
        verify(orderService).updateOrder(captor.capture());
        assertEquals(orderDto, captor.getValue());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoDelete() throws ServletException, IOException {
        Long orderId = 1L;
        OrderDto orderDto = new OrderDto(orderId, 1L, Arrays.asList(1L, 2L));

        when(request.getPathInfo()).thenReturn("/" + orderId);
        when(orderService.deleteOrder(orderId)).thenReturn(orderDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        orderServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(orderDto);
        assertEquals(expectedJson, stringWriter.toString());
    }
}