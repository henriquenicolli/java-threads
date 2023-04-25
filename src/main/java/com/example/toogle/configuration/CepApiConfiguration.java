package com.example.toogle.configuration;

import com.example.toogle.component.DefaultCepApiStrategy;
import com.example.toogle.component.FeatureCepApiStrategy;
import com.example.toogle.component.strategy.CepApiStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CepApiConfiguration {

    @Autowired
    private DefaultCepApiStrategy defaultStrategy;

    @Autowired
    private FeatureCepApiStrategy featureStrategy;

    @Value("${myapp.feature1.enabled}")
    private Boolean newApi;

    @Bean
    public CepApiStrategy cepApiStrategy() {
        if (newApi) {
            return featureStrategy;
        } else {
            return defaultStrategy;
        }
    }
}