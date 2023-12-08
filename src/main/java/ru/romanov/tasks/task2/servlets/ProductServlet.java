package ru.romanov.tasks.task2.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.romanov.tasks.task2.dto.ProductWithPrice;
import ru.romanov.tasks.task2.service.ProductService;
import ru.romanov.tasks.task2.service.impl.ProductServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

@WebServlet(name = "ProductServlet", value = "/api/v1/market/products/*")
public class ProductServlet extends HttpServlet {
    private static final String JSON_TYPE = "application/json";
    private static final String BAD_REQUEST_STR = "Bad request: ";
    private final ProductService productService;
    private final ObjectMapper objectMapper;

    public ProductServlet() {
        this.productService = new ProductServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    public ProductServlet(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

    private void handleGetAllProducts(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ProductWithPrice> allProducts = productService.getProducts(0, 25);
        String json = objectMapper.writeValueAsString(allProducts);
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().write(json);
    }

    private void handleGetProductById(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        String idString = pathInfo.substring(1);
        try {
            UUID productId = UUID.fromString(idString);
            ProductWithPrice product = productService.getProduct(productId);

            if (product == null) {
                resp.setStatus(SC_NOT_FOUND);
                System.out.println("Product not found");
                return;
            }
            String json = objectMapper.writeValueAsString(product);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        resp.setContentType(JSON_TYPE);

        if (pathInfo == null || "/".equals(pathInfo)) {
            handleGetAllProducts(req, resp);
        } else {
            handleGetProductById(req, resp, pathInfo);
        }
    }

    private void handleCreateProduct(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ProductWithPrice product = objectMapper.readValue(req.getReader(), ProductWithPrice.class);
            productService.createProduct(product);

            resp.setStatus(SC_CREATED);
            System.out.println("Product created successfully");
        } catch (JsonProcessingException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if ("/create".equals(pathInfo)) {
            resp.setContentType(JSON_TYPE);
            handleCreateProduct(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleUpdateProduct(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        String idString = pathInfo.substring(8);
        try {
            UUID productId = UUID.fromString(idString);
            ProductWithPrice product = objectMapper.readValue(req.getReader(), ProductWithPrice.class);
            productService.changeProduct(productId, product);
            resp.setStatus(SC_OK);
            System.out.println("Product updated successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.matches("/update/[^/]+")) {
            resp.setContentType(JSON_TYPE);
            handleUpdateProduct(req, resp, pathInfo);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }

    private void handleRemoveProduct(HttpServletRequest req, HttpServletResponse resp, String pathInfo) {
        String idString = pathInfo.substring(8);
        try {
            UUID productId = UUID.fromString(idString);

            productService.removeProduct(productId);

            resp.setStatus(HttpServletResponse.SC_OK);
            System.out.println("Product removed successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/remove/[^/]+")) {
            resp.setContentType(JSON_TYPE);
            handleRemoveProduct(req, resp, pathInfo);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }
}
