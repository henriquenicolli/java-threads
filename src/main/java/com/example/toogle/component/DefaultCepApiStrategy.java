package com.example.toogle.component;

import com.example.toogle.component.strategy.CepApiStrategy;
import org.springframework.stereotype.Component;

@Component
public class DefaultCepApiStrategy implements CepApiStrategy {

    @Override
    public String buscaCep() {
        // default
        return "Busca realizada pela api padrao";
    }

}
