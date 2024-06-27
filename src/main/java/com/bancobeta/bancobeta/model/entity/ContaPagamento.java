package com.bancobeta.bancobeta.model.entity;

import com.bancobeta.bancobeta.exception.ExcecoesBancarias;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CP")
public class ContaPagamento extends Conta {
    private double limiteTransferencia = 4999.99;
    private int quantidadeSaquesGratuitos = 4;
    private static final double TARIFA_SAQUE = 6.50;

    // Construtores
    public ContaPagamento() {
        // Construtor vazio para JPA
    }

    public ContaPagamento(String numeroConta, double saldo, Cliente cliente) {
        super(numeroConta, saldo, cliente);
    }

    // Getters e Setters
    public double getLimiteTransferencia() {
        return limiteTransferencia;
    }

    public void setLimiteTransferencia(double limiteTransferencia) {
        this.limiteTransferencia = limiteTransferencia;
    }

    @Override
    public void transferir(Conta contaDestino, double valor) throws ExcecoesBancarias.SaldoInsuficienteException, ExcecoesBancarias.LimiteTransferenciaExcedidoException {
        if (valor > 0 && valor <= this.getSaldo()) {
            if (valor <= this.limiteTransferencia) {
                super.transferir(contaDestino, valor);
            } else {
                throw new ExcecoesBancarias.LimiteTransferenciaExcedidoException("Limite de transferência excedido.");
            }
        } else {
            throw new ExcecoesBancarias.SaldoInsuficienteException("Saldo insuficiente para transferência.");
        }
    }

    @Override
    public void sacar(double valor) throws ExcecoesBancarias.SaldoInsuficienteException {
        if (valor > 0 && valor <= this.getSaldo()) {
            if (quantidadeSaquesGratuitos > 0) {
                quantidadeSaquesGratuitos--;
            } else {
                this.setSaldo(this.getSaldo() - TARIFA_SAQUE); // Cobra a tarifa
            }
            super.sacar(valor);
        } else {
            throw new ExcecoesBancarias.SaldoInsuficienteException("Saldo insuficiente para saque.");
        }
    }

    @Override
    public double calcularTarifa() {
        int saquesPagos = Math.max(0, getTransacoes().size() - quantidadeSaquesGratuitos);
        return saquesPagos * TARIFA_SAQUE;
    }

}
