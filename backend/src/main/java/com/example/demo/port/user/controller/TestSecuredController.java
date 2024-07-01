package com.example.demo.port.user.controller;

import com.example.demo.port.shoppingcart.producer.UpdateProdcutProducer;
import com.example.demo.port.user.producer.ProductProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/secured")
public class TestSecuredController {

    @GetMapping("/test")
    public String getSecuredTestData() {
        return "Success! You accessed a secured resource.";
    }
}
