package com.stockexchangeapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockexchangeapi.base.TestBase;
import com.stockexchangeapi.configuration.I18nConfiguration;
import com.stockexchangeapi.model.request.CreateStockExchangeRequest;
import com.stockexchangeapi.model.request.CreateStockRequest;
import com.stockexchangeapi.model.response.CreateStockResponse;
import com.stockexchangeapi.model.response.GetStockResponse;
import com.stockexchangeapi.service.JwtService;
import com.stockexchangeapi.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {StockController.class, I18nConfiguration.class, JwtService.class})
@WithMockUser(username = "test")
public class StockControllerTests extends TestBase {
    private static final String ENDPOINT = "/api/stock";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private StockService stockService;
    @Autowired
    private MessageSource messageSource;
    @Test
    void it_should_return_ok_when_getting_all_stocks_succeed() throws Exception {

        List<GetStockResponse> getStockResponseList = dataGenerator.objects(GetStockResponse.class, 3).toList();
        when(stockService.getAll()).thenReturn(getStockResponseList);

        String responseJson = mapper.writeValueAsString(getStockResponseList);
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    void it_should_return_ok_when_getting_stock_succeed() throws Exception {
        GetStockResponse getStockResponse = dataGenerator.nextObject(GetStockResponse.class);
        when(stockService.getByName(getStockResponse.getName())).thenReturn(getStockResponse);

        String responseJson = mapper.writeValueAsString(getStockResponse);
        mockMvc.perform(get(ENDPOINT + "/" + getStockResponse.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    void it_should_return_created_when_create_stock_succeed() throws Exception {

        CreateStockRequest createStockRequest = dataGenerator.nextObject(CreateStockRequest.class);
        CreateStockResponse createStockResponse = new CreateStockResponse(1L);
        when(stockService.create(any())).thenReturn(createStockResponse);

        String responseJson = mapper.writeValueAsString(createStockResponse);
        mockMvc.perform(post(ENDPOINT).with(csrf()).content(mapper.writeValueAsString(createStockRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson));
    }

    @Test
    void it_should_return_no_content_when_delete_stock_succeed() throws Exception {
        String stockName = "test-stock";
        doNothing().when(stockService).delete(stockName);

        mockMvc.perform(delete(ENDPOINT + "/" + stockName)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void it_should_return_no_content_when_update_stock_price_succeed() throws Exception {
        String stockName = "test-stock";
        BigDecimal updatedPrice = BigDecimal.TEN;
        doNothing().when(stockService).updatePrice(stockName, updatedPrice);

        mockMvc.perform(put(ENDPOINT + "/" + stockName + "/current-price")
                        .with(csrf())
                        .param("price", String.valueOf(updatedPrice))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void it_should_return_bad_request_when_stock_name_null() throws Exception {
        CreateStockRequest createStockRequest = dataGenerator.nextObject(CreateStockRequest.class);
        createStockRequest.setName(null);
        var message = messageSource.getMessage("api.validation.constraints.stock.name.message", null, Locale.ENGLISH);

        mockMvc.perform(post(ENDPOINT).with(csrf())
                        .content(mapper.writeValueAsString(createStockRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value(message));
    }
    @Test
    void it_should_return_bad_request_when_stock_description_null() throws Exception {
        CreateStockRequest createStockRequest = dataGenerator.nextObject(CreateStockRequest.class);
        createStockRequest.setDescription(null);
        var message = messageSource.getMessage("api.validation.constraints.stock.description.message", null, Locale.ENGLISH);

        mockMvc.perform(post(ENDPOINT).with(csrf())
                        .content(mapper.writeValueAsString(createStockRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value(message));
    }

    @Test
    public void it_should_throw_internal_server_error_when_creating_stock_failed() throws Exception {
        CreateStockRequest createStockRequest = dataGenerator.nextObject(CreateStockRequest.class);
        when(stockService.create(any())).thenThrow(new RuntimeException());
        mockMvc.perform(post(ENDPOINT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createStockRequest)))
                .andExpect(status().isInternalServerError());
    }
}
