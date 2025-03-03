package bakery.servlets;

import bakery.dto.ProductDto;
import bakery.mapper.ProductMapper;
import bakery.repository.ProductRepository;
import bakery.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Сервлет для управления продуктами, обрабатывающий HTTP-запросы для создания, получения, обновления и удаления продуктов
 */
@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * Инициализирует сервлет и создает экземпляр ProductService
     *
     * @throws ServletException если не удается инициализировать сервлет
     */
    @Override
    public void init() throws ServletException {
        ProductRepository productRepository = new ProductRepository();
        ProductMapper productMapper = new ProductMapper();
        productService = new ProductService(productRepository, productMapper);
    }
    /**
     * Обрабатывает GET-запросы для получения информации о продуктах
     * Если указан id продукта, возвращает информацию о конкретном продукте.
     * Если id не указан, возвращает список всех продуктов
     *
     * @param request  объект HttpServletRequest, содержащий информацию о запросе
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
                ProductDto productDto = productService.getProductById(id);
                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(productDto));
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid product ID");
            }
        } else {
            List<ProductDto> products = productService.getAllProducts();
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(products));
        }
    }

    /**
     * Обрабатывает GET-запросы для получения информации о продуктах
     * Если указан id продукта, возвращает информацию о конкретном продукте
     * Если id не указан, возвращает список всех продуктов
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDto productDto = objectMapper.readValue(request.getInputStream(), ProductDto.class);
        ProductDto createdProduct = productService.createProduct(productDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(createdProduct));
    }

    /**
     * Обрабатывает PUT-запросы для обновления существующего продукта
     *
     * @param request объект HttpServletRequest, содержащий информацию о запросе
     * @param response объект HttpServletResponse, используемый для отправки ответа
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException  если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDto productDto = objectMapper.readValue(request.getInputStream(), ProductDto.class);
        productService.updateProduct(productDto);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    /**
     * Обрабатывает DELETE-запросы для удаления продукта по его id.
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
                ProductDto deletedProduct = productService.deleteProduct(id);
                if (deletedProduct != null) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().write(objectMapper.writeValueAsString(deletedProduct));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    response.getWriter().write("Product not found");
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Invalid product ID");
            }
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
