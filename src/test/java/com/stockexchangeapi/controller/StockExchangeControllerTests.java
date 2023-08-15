package com.stockexchangeapi.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockexchangeapi.base.TestBase;
import com.stockexchangeapi.configuration.I18nConfiguration;
import com.stockexchangeapi.model.request.AddStockToExchangeRequest;
import com.stockexchangeapi.model.request.CreateStockExchangeRequest;
import com.stockexchangeapi.model.response.CreateStockExchangeResponse;
import com.stockexchangeapi.model.response.StockExchangeResponse;
import com.stockexchangeapi.service.JwtService;
import com.stockexchangeapi.service.StockExchangeService;
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

import java.util.List;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = {StockExchangeController.class, I18nConfiguration.class, JwtService.class})
@WithMockUser(username = "test")
public class StockExchangeControllerTests extends TestBase {
    private static final String ENDPOINT = "/api/stock-exchange";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private StockExchangeService stockExchangeService;
    @Autowired
    private MessageSource messageSource;

    @Test
    void it_should_return_ok_when_getting_all_stock_exchanges_succeed() throws Exception {

        List<StockExchangeResponse> getStockExchangeResponseList = dataGenerator.objects(StockExchangeResponse.class, 3).toList();
        when(stockExchangeService.getAll()).thenReturn(getStockExchangeResponseList);

        String responseJson = mapper.writeValueAsString(getStockExchangeResponseList);
        mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    void it_should_return_ok_when_getting_stock_exchange_succeed() throws Exception {
        StockExchangeResponse stockExchangeResponse = dataGenerator.nextObject(StockExchangeResponse.class);
        when(stockExchangeService.getByName(stockExchangeResponse.getName())).thenReturn(stockExchangeResponse);

        String responseJson = mapper.writeValueAsString(stockExchangeResponse);
        mockMvc.perform(get(ENDPOINT + "/" + stockExchangeResponse.getName()))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }

    @Test
    void it_should_return_created_when_create_stock_exchange_succeed() throws Exception {

        CreateStockExchangeRequest createStockExchangeRequest = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        CreateStockExchangeResponse createStockExchangeResponse = new CreateStockExchangeResponse(1L, false);
        when(stockExchangeService.create(any())).thenReturn(createStockExchangeResponse);

        String responseJson = mapper.writeValueAsString(createStockExchangeResponse);
        mockMvc.perform(post(ENDPOINT).with(csrf()).content(mapper.writeValueAsString(createStockExchangeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseJson));
    }

    @Test
    void it_should_return_no_content_when_delete_stock_exchange_succeed() throws Exception {
        StockExchangeResponse stockExchangeResponse = dataGenerator.nextObject(StockExchangeResponse.class);
        stockExchangeResponse.setName("stock-exchange");
        String stockName = "stock";
        when(stockExchangeService.deleteStock(stockExchangeResponse.getName(), stockName)).thenReturn(stockExchangeResponse);

        mockMvc.perform(delete(ENDPOINT + "/" + stockExchangeResponse.getName())
                        .with(csrf())
                        .param("stockName", stockName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void it_should_return_created_when_add_stock_to_exchange_succeed() throws Exception {
        AddStockToExchangeRequest addStockToExchangeRequest = dataGenerator.nextObject(AddStockToExchangeRequest.class);
        when(stockExchangeService.addStock(addStockToExchangeRequest)).thenReturn(new StockExchangeResponse());

        mockMvc.perform(post(ENDPOINT + "/" + addStockToExchangeRequest.getStockExchangeName())
                        .content(mapper.writeValueAsString(addStockToExchangeRequest))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void it_should_return_bad_request_when_stock_exchange_name_null() throws Exception {
        var message = messageSource.getMessage("api.validation.constraints.stock.exchange.name.message", null, Locale.ENGLISH);

        CreateStockExchangeRequest createStockExchangeRequest = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        createStockExchangeRequest.setName(null);

        mockMvc.perform(post(ENDPOINT).with(csrf()).content(mapper.writeValueAsString(createStockExchangeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value(message));
    }

    @Test
    void it_should_return_bad_request_when_stock_exchange_description_null() throws Exception {
        var message = messageSource.getMessage("api.validation.constraints.stock.exchange.description.message", null, Locale.ENGLISH);

        CreateStockExchangeRequest createStockExchangeRequest = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        createStockExchangeRequest.setDescription(null);

        mockMvc.perform(post(ENDPOINT).with(csrf()).content(mapper.writeValueAsString(createStockExchangeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].message").value(message));
    }

    @Test
    public void it_should_throw_internal_server_error_when_creating_stock_exchange_failed() throws Exception {
        CreateStockExchangeRequest createStockExchangeRequest = dataGenerator.nextObject(CreateStockExchangeRequest.class);
        when(stockExchangeService.create(any())).thenThrow(new RuntimeException());

        mockMvc.perform(post(ENDPOINT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createStockExchangeRequest)))
                .andExpect(status().isInternalServerError());
    }
}
