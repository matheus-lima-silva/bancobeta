package com.bancobeta.bancobeta.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTransacao;

    private double valor;
    private String tipo;
    private LocalDateTime dataHora;

    @ManyToOne
    @JoinColumn(name = "conta_origem_id")
    private Conta contaOrigem;

    @ManyToOne
    @JoinColumn(name = "conta_destino_id")
    private Conta contaDestino;

    public Transacao(double valor, String tipo, Conta contaOrigem, Conta contaDestino) {
        this.valor = valor;
        this.tipo = tipo;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        //configurar gmt
        this.dataHora = LocalDateTime.now();
    }

    // Construtores, getters e setters
    // ...

    // Métodos adicionais, se necessário
    // ...
}

