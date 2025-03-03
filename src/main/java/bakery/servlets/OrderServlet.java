package bakery.servlets;

import bakery.dto.OrderDto;
import bakery.mapper.OrderMapper;
import bakery.repository.OrderRepository;
import bakery.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет для управления заказами, обрабатывающий HTTP-запросы для создания, получения, обновления и удаления заказов
 */
@WebServlet("/orders/*")
public class OrderServlet extends HttpServlet {
    OrderService orderService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Инициализирует сервлет и создает экземпляр OrderService
     *
     * @throws ServletException если не удается инициализировать сервлет
     */
    @Override
    public void init() throws ServletException {
        OrderRepository orderRepository = new OrderRepository();
        OrderMapper orderMapper = new OrderMapper(orderRepository);
        orderService = new OrderService(orderRepository, orderMapper);
    }

    /**
     * Обрабатывает GET-запросы для получения информации о заказах
     * Если указан id заказа, возвращает информацию о конкретном заказе
     * Если id не указан, возвращает список всех заказов
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }

        if (pathInfo != null && !pathInfo.isEmpty()) {
            try {
                Long id = Long.valueOf(pathInfo);
                OrderDto orderDto = orderService.getOrderById(id);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(orderDto));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid order ID");
            }
        } else {
            List<OrderDto> orders = orderService.getAllOrders();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(orders));
        }
    }

    /**
     * Обрабатывает POST-запросы для создания нового заказа
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDto orderDto = objectMapper.readValue(request.getInputStream(), OrderDto.class);
        OrderDto createdOrder = orderService.createOrder(orderDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(createdOrder));
    }

    /**
     * Обрабатывает PUT-запросы для обновления существующего заказа
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        OrderDto orderDto = objectMapper.readValue(request.getInputStream(), OrderDto.class);
        orderService.updateOrder(orderDto);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Обрабатывает DELETE-запросы для удаления заказа по его id
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }

        if (pathInfo != null && !pathInfo.isEmpty()) {
            try {
                Long id = Long.valueOf(pathInfo);
                OrderDto deletedOrder = orderService.deleteOrder(id);
                if (deletedOrder != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(deletedOrder));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Order not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid order ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
