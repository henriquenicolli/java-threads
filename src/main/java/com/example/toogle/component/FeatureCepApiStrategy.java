package com.example.toogle.component;

import com.example.toogle.component.strategy.CepApiStrategy;
import org.springframework.stereotype.Component;

@Component
public class FeatureCepApiStrategy implements CepApiStrategy {

    @Override
    public String buscaCep() {
        // implementa o novo comportamento
        return "Busca realizada pela nova api de cep";
    }

}
