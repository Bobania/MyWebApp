package bakery.servlets;

import bakery.dto.ClientDto;
import bakery.service.ClientService;
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

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServletTest {
    @Mock
    private ClientService clientService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ClientServlet clientServlet;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientServlet.clientService = clientService;
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDoGet_WithId() throws ServletException, IOException {
        Long clientId = 1L;
        ClientDto clientDto = new ClientDto(clientId, "John", "Doe", "123456789");

        when(request.getPathInfo()).thenReturn("/" + clientId);
        when(clientService.getClientById(clientId)).thenReturn(clientDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        clientServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(clientDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoGet_WithoutId() throws ServletException, IOException {
        List<ClientDto> clients = Arrays.asList(
                new ClientDto(1L, "John", "Doe", "123456789"),
                new ClientDto(2L, "Jane", "Smith", "987654321")
        );

        when(request.getPathInfo()).thenReturn(null);
        when(clientService.getAllClients()).thenReturn(clients);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        clientServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(clients);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        ClientDto clientDto = new ClientDto(null, "John", "Doe", "123456789");
        ClientDto createdClientDto = new ClientDto(1L, "John", "Doe", "123456789");

        byte[] jsonBytes = objectMapper.writeValueAsBytes(clientDto);
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
        when(clientService.createClient(any(ClientDto.class))).thenReturn(createdClientDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        clientServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(createdClientDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPut() throws ServletException, IOException {
        ClientDto clientDto = new ClientDto(1L, "John", "Doe", "123456789");

        byte[] jsonBytes = objectMapper.writeValueAsBytes(clientDto);
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

        clientServlet.doPut(request, response);

        ArgumentCaptor<ClientDto> captor = ArgumentCaptor.forClass(ClientDto.class);
        verify(clientService).updateClient(captor.capture());
        assertEquals(clientDto, captor.getValue());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }
    @Test
    public void testDoDelete() throws ServletException, IOException {
        Long clientId = 1L;
        ClientDto clientDto = new ClientDto(clientId, "John", "Doe", "123456789");

        when(request.getPathInfo()).thenReturn("/" + clientId);
        when(clientService.deleteClient(clientId)).thenReturn(clientDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        clientServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(clientDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

}