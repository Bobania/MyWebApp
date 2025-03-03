package bakery.servlets;

import bakery.dto.ClientDto;
import bakery.mapper.ClientMapper;
import bakery.mapper.Mapper;
import bakery.repository.ClientRepository;
import bakery.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет для управления клиентами, обрабатывающий HTTP-запросы для создания, получения, обновления и удаления клиентов
 */
@WebServlet("/clients/*")
public class ClientServlet extends HttpServlet {
    protected ClientService clientService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Инициализирует сервлет и создает экземпляр ClientService
     *
     * @throws ServletException если не удается инициализировать сервлет
     */
    @Override
    public void init() throws ServletException {
        // Инициализация ClientService здесь
        ClientRepository clientRepository = new ClientRepository();
        ClientMapper clientMapper = new ClientMapper();
        clientService = new ClientService(clientRepository, clientMapper);
    }

    /**
     * Обрабатывает GET-запросы для получения информации о клиентах
     * Если указан id клиента, возвращает информацию о конкретном клиенте
     * Если id не указан, возвращает список всех клиентов
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
                ClientDto clientDto = clientService.getClientById(id);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(clientDto));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid client ID");
            }
        } else {
            List<ClientDto> clients = clientService.getAllClients();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(clients));
        }
    }

    /**
     * Обрабатывает GET-запросы для получения информации о клиентах
     * Если указан id клиента, возвращает информацию о конкретном клиенте
     * Если id не указан, возвращает список всех клиентов
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientDto clientDto = objectMapper.readValue(request.getInputStream(), ClientDto.class);
        ClientDto createdClient = clientService.createClient(clientDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(createdClient));
    }

    /**
     * Обрабатывает PUT-запросы для обновления существующего клиента.
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientDto clientDto = objectMapper.readValue(request.getInputStream(), ClientDto.class);
        clientService.updateClient(clientDto);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Обрабатывает DELETE-запросы для удаления клиента по его id.
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
                ClientDto deletedClient = clientService.deleteClient(id);
                if (deletedClient != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(deletedClient));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Client not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid client ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

}
