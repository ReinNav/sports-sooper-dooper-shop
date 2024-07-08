package com.productService.port.user.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secured")
public class TestSecuredController {

    @GetMapping("/test")
    public String getSecuredTestData() {
        return "Success! You accessed a secured resource.";
    }
}
