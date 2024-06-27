package com.bancobeta.bancobeta.model.entity;

import com.bancobeta.bancobeta.exception.ExcecoesBancarias;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CC")
public class ContaCorrente extends Conta {

    private double limiteChequeEspecial;
    private int quantidadeSaquesGratuitos = 4;
    private static final double TARIFA_SAQUE = 6.50;

    public ContaCorrente() {
    }

    public ContaCorrente(String numeroConta, double saldo, Cliente cliente, double limiteChequeEspecial) {
        super(numeroConta, saldo, cliente);
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    public double getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(double limiteChequeEspecial) {
        this.limiteChequeEspecial = limiteChequeEspecial;
    }

    @Override
    public void sacar(double valor) throws ExcecoesBancarias.SaldoInsuficienteException {
        if (valor > 0) {
            double saldoDisponivel = getSaldo() + limiteChequeEspecial;
            if (valor <= saldoDisponivel) {
                if (quantidadeSaquesGratuitos > 0) {
                    quantidadeSaquesGratuitos--;
                } else {
                    setSaldo(getSaldo() - TARIFA_SAQUE);
                }
                super.sacar(valor);
            } else {
                throw new ExcecoesBancarias.SaldoInsuficienteException("Saldo e limite de cheque especial insuficientes.");
            }
        } else {
            throw new IllegalArgumentException("Valor do saque deve ser positivo.");
        }
    }

    @Override
    public double calcularTarifa() {

        double tarifaChequeEspecial = 0.0;
        if (getSaldo() < 0) {
            double saldoNegativo = -getSaldo();
            tarifaChequeEspecial = saldoNegativo * 0.01;
        }


        int saquesPagos = Math.max(0, getTransacoes().size() - quantidadeSaquesGratuitos);
        double tarifaSaque = saquesPagos * TARIFA_SAQUE;

        
        return tarifaChequeEspecial + tarifaSaque;
    }
}
