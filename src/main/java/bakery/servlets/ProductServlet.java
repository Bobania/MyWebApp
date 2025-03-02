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

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {
    private ProductService productService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        ProductRepository productRepository = new ProductRepository();
        ProductMapper productMapper = new ProductMapper();
        productService = new ProductService(productRepository, productMapper);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDto productDto = objectMapper.readValue(request.getInputStream(), ProductDto.class);
        ProductDto createdProduct = productService.createProduct(productDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(createdProduct));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDto productDto = objectMapper.readValue(request.getInputStream(), ProductDto.class);
        productService.updateProduct(productDto);
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
