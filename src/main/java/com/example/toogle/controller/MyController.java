package com.example.toogle.controller;

import com.example.toogle.component.strategy.CepApiStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {

    @Autowired
    private CepApiStrategy cepApiStrategy;

    @GetMapping("/cep")
    public ResponseEntity<String> doCep() {
        return ResponseEntity.ok(cepApiStrategy.buscaCep());
    }
}
