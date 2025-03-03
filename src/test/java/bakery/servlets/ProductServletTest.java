package bakery.servlets;

import bakery.dto.ProductDto;
import bakery.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import org.mockito.junit.jupiter.MockitoExtension;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServletTest {

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private ProductServlet productServlet;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        productServlet.productService = productService;
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDoGet_WithId() throws ServletException, IOException {
        Long productId = 1L;
        ProductDto productDto = new ProductDto(productId, "Cake", 10.99);

        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productService.getProductById(productId)).thenReturn(productDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(productDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoGet_WithoutId() throws ServletException, IOException {
        List<ProductDto> products = Arrays.asList(
                new ProductDto(1L, "Cake", 10.99),
                new ProductDto(2L, "Bread", 2.99)
        );

        when(request.getPathInfo()).thenReturn(null);
        when(productService.getAllProducts()).thenReturn(products);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        productServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(products);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        ProductDto productDto = new ProductDto(null, "Cake", 10.99);
        ProductDto createdProductDto = new ProductDto(1L, "Cake", 10.99);

        byte[] jsonBytes = objectMapper.writeValueAsBytes(productDto);
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
        when(productService.createProduct(any(ProductDto.class))).thenReturn(createdProductDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        productServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(createdProductDto);
        assertEquals(expectedJson, stringWriter.toString());
    }

    @Test
    public void testDoPut() throws ServletException, IOException {
        ProductDto productDto = new ProductDto(1L, "Cake", 10.99);

        byte[] jsonBytes = objectMapper.writeValueAsBytes(productDto);
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

        productServlet.doPut(request, response);

        ArgumentCaptor<ProductDto> captor = ArgumentCaptor.forClass(ProductDto.class);
        verify(productService).updateProduct(captor.capture());
        assertEquals(productDto, captor.getValue());
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    public void testDoDelete() throws ServletException, IOException {
        Long productId = 1L;
        ProductDto productDto = new ProductDto(productId, "Cake", 10.99);

        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productService.deleteProduct(productId)).thenReturn(productDto);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        productServlet.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response).setContentType("application/json");
        String expectedJson = objectMapper.writeValueAsString(productDto);
        assertEquals(expectedJson, stringWriter.toString());
    }
}
