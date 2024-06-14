package com.bancobeta.bancobeta.model.entity;

import com.bancobeta.bancobeta.exception.ExcecoesBancarias;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoConta")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContaCorrente.class, name = "CC"),
        @JsonSubTypes.Type(value = ContaPagamento.class, name = "CP")
})
public abstract class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroConta;
    private double saldo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "contaOrigem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes = new ArrayList<>();

    public Conta() {
    }

    public Conta(String numeroConta, double saldo, Cliente cliente) {
        this.numeroConta = numeroConta;
        this.saldo = saldo;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(String numeroConta) {
        this.numeroConta = numeroConta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldo += valor;
            registrarTransacao("Depósito", valor);
        } else {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo.");
        }
    }

    public void sacar(double valor) throws ExcecoesBancarias.SaldoInsuficienteException {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            registrarTransacao("Saque", valor);
        } else {
            throw new ExcecoesBancarias.SaldoInsuficienteException("Saldo insuficiente para saque.");
        }
    }

    public void transferir(Conta contaDestino, double valor) throws ExcecoesBancarias.SaldoInsuficienteException {
        if (valor > 0 && valor <= this.saldo) {
            this.saldo -= valor;
            contaDestino.depositar(valor);
            registrarTransacao("Transferência para " + contaDestino.getNumeroConta(), valor);
        } else {
            throw new ExcecoesBancarias.SaldoInsuficienteException("Saldo insuficiente para transferência.");
        }
    }


    protected void registrarTransacao(String tipo, double valor) {
        Transacao transacao = new Transacao(valor, tipo, this, null);
        transacoes.add(transacao);
    }

    public abstract double calcularTarifa();
}
