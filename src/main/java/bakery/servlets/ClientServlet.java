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

import java.io.IOException;
import java.util.List;

@WebServlet("/clients/*")
public class ClientServlet extends HttpServlet {
    private ClientService clientService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        // Инициализация ClientService здесь
        ClientRepository clientRepository = new ClientRepository();
        ClientMapper clientMapper = new ClientMapper();
        clientService = new ClientService(clientRepository, clientMapper);
    }

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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientDto clientDto = objectMapper.readValue(request.getInputStream(), ClientDto.class);
        ClientDto createdClient = clientService.createClient(clientDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(createdClient));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ClientDto clientDto = objectMapper.readValue(request.getInputStream(), ClientDto.class);
        clientService.updateClient(clientDto);
        response.setStatus(HttpServletResponse.SC_OK);
    }

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
