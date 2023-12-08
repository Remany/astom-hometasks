package ru.romanov.tasks.task2.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.romanov.tasks.task2.dto.ConsumerDto;
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
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;


@WebServlet(name = "ConsumerServlet", value = "/api/v1/market/client/*")
public class ConsumerServlet extends HttpServlet {
    private static final String JSON_TYPE = "application/json";
    private static final String BAD_REQUEST_STR = "Bad request: ";
    private final ConsumerService consumerService;
    private final ObjectMapper objectMapper;

    public ConsumerServlet() {
        this.consumerService = new ConsumerServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    public ConsumerServlet(ConsumerService consumerService, ObjectMapper objectMapper) {
        this.consumerService = consumerService;
        this.objectMapper = objectMapper;
    }

    private void handleGetConsumerById(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        String idString = pathInfo.substring(1);
        try {
            UUID consumerId = UUID.fromString(idString);

            ConsumerDto consumerDto = consumerService.getConsumer(consumerId);

            if (consumerDto == null) {
                resp.setStatus(SC_NOT_FOUND);
                System.out.println("Consumer not found");
                return;
            }
            String json = objectMapper.writeValueAsString(consumerDto);
            resp.setContentType(JSON_TYPE);
            resp.setStatus(SC_OK);
            resp.getWriter().write(json);
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println(BAD_REQUEST_STR + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/[a-f0-9\\-]+")) {
            resp.setContentType(JSON_TYPE);
            handleGetConsumerById(req, resp, pathInfo);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }

    private void handleCreateConsumer(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            ConsumerDto consumerDto = objectMapper.readValue(req.getReader(), ConsumerDto.class);
            consumerService.createConsumer(consumerDto);

            resp.setStatus(SC_CREATED);
            System.out.println("Consumer created successfully");
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
            handleCreateConsumer(req, resp);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }
}
