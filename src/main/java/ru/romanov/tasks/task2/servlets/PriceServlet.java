package ru.romanov.tasks.task2.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.romanov.tasks.task2.dto.PriceDto;
import ru.romanov.tasks.task2.service.PriceService;
import ru.romanov.tasks.task2.service.impl.PriceServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;

@WebServlet(name = "PriceServlet", value = "/api/v1/market/price/*")
public class PriceServlet extends HttpServlet {
    private static final String JSON_TYPE = "application/json";
    private final PriceService priceService;
    private final ObjectMapper objectMapper;

    public PriceServlet() {
        this.priceService = new PriceServiceImpl();
        this.objectMapper = new ObjectMapper();
    }

    public PriceServlet(PriceService priceService, ObjectMapper objectMapper) {
        this.priceService = priceService;
        this.objectMapper = objectMapper;
    }

    private void handlePatchProduct(HttpServletRequest req, HttpServletResponse resp, String pathInfo) throws IOException {
        String idString = pathInfo.substring(1);
        try {
            UUID productId = UUID.fromString(idString);

            PriceDto priceDto = objectMapper.readValue(req.getReader(), PriceDto.class);
            priceService.changePrice(productId, priceDto);

            resp.setStatus(SC_OK);
            System.out.println("Product price updated successfully");
        } catch (NumberFormatException e) {
            resp.setStatus(SC_BAD_REQUEST);
            System.out.println("Bad request: " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.matches("/[^/]+")) {
            resp.setContentType(JSON_TYPE);
            handlePatchProduct(req, resp, pathInfo);
        } else {
            resp.setStatus(SC_NOT_FOUND);
        }
    }
}
