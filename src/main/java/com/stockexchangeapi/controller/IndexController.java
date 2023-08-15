package com.stockexchangeapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Hidden
public class IndexController {

    @GetMapping
    public RedirectView redirectToSwaggerUi(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache");
        return new RedirectView("/swagger-ui/index.html");
    }
}