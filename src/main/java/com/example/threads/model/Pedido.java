package com.example.threads.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Pedido {

    public Pedido () {}
    public Pedido(String descricao, BigDecimal valor) {
        this.descricao = descricao;
        this.valor = valor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private BigDecimal valor;

    private LocalDateTime dataCriacao;

    @Version
    private Long versao;

}
