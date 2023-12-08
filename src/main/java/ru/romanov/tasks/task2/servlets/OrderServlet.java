package ru.romanov.tasks.task2.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.romanov.tasks.task2.dto.OrderDto;
import ru.romanov.tasks.task2.service.ConsumerService;
import ru.romanov.tasks.task2.service.impl.ConsumerServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

@WebServlet(name = "OrderServlet", value = "/api/v1/market/order/buy/*")
public class OrderServlet extends HttpServlet {
    private static final String JSON_TYPE = "application/json";
    private static final String BAD_REQUEST_STR = "Bad request: ";
    private final ConsumerService consumerService;
    private final ObjectMapper objectMapper;

    public OrderServlet() {
        this.consumerService = new ConsumerServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    public OrderServlet(ConsumerService consumerService, ObjectMapper objectMapper) {
        this.consumerService = consumerService;
        this.objectMapper = objectMapper;
    }

    private void handleBuyProduct(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        String idString = pathInfo.substring(1);
        try {
            UUID consumerId = UUID.fromString(idString);
            OrderDto orderDto = objectMapper.readValue(req.getReader(), OrderDto.class);
            consumerService.buyProduct(consumerId, orderDto.getProductId());

            resp.setStatus(SC_OK);
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + e.getMessage());
        } catch (JsonProcessingException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + "Invalid JSON format");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/[a-f0-9\\-]+")) {
            resp.setContentType(JSON_TYPE);
            handleBuyProduct(req, resp, pathInfo);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }
}
