package com.bancobeta.bancobeta.service;


import com.bancobeta.bancobeta.exception.ExcecoesBancarias;
import com.bancobeta.bancobeta.exception.ExcecoesBancarias.ContaInvalidaException;
import com.bancobeta.bancobeta.exception.ExcecoesBancarias.SaldoInsuficienteException;
import com.bancobeta.bancobeta.model.entity.*;
import com.bancobeta.bancobeta.repository.ContaRepository;
import com.bancobeta.bancobeta.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContaService {


    private final ContaRepository contaRepository;


    private final TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public Conta criarConta(Conta conta) {
        if (conta == null || conta.getCliente() == null) {
            throw new IllegalArgumentException("Dados da conta ou cliente inválidos.");
        }

        return contaRepository.save(conta);
    }




    private void validarLimiteChequeEspecial(ContaCorrente contaCorrente) {
        if (contaCorrente.getLimiteChequeEspecial() < 0) {
            throw new IllegalArgumentException("Limite de cheque especial não pode ser negativo.");
        }
    }

    private void validarLimiteTransferencia(ContaPagamento contaPagamento) {
        if (contaPagamento.getLimiteTransferencia() < 0) {
            throw new IllegalArgumentException("Limite de transferência não pode ser negativo.");
        }
    }


    public Conta obterContaPorNumero(String numeroConta) {
        return contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ContaInvalidaException("Conta não encontrada com o número: " + numeroConta));
    }

    public List<Conta> obterContasPorCliente(Long clienteId) {
        return contaRepository.findByClienteId(clienteId);
    }

    public double consultarSaldo(String numeroConta) {
        Conta conta = obterContaPorNumero(numeroConta);
        return conta.getSaldo();
    }

    public void depositar(String numeroConta, double valor) {
        Conta conta = obterContaPorNumero(numeroConta);
        conta.setSaldo(conta.getSaldo() + valor);
        contaRepository.save(conta);

        Transacao transacao = new Transacao(valor, "Depósito", null, conta);
        transacaoRepository.save(transacao);
    }

    public void sacar(String numeroConta, double valor) {
        Conta conta = obterContaPorNumero(numeroConta);

        if (valor <= 0) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo.");
        }

        if (conta instanceof ContaCorrente) {
            ContaCorrente contaCorrente = (ContaCorrente) conta;
            if (contaCorrente.getSaldo() + contaCorrente.getLimiteChequeEspecial() < valor) {
                throw new SaldoInsuficienteException("Saldo insuficiente para saque.");
            }
        } else if (conta.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente para saque.");
        }

        conta.setSaldo(conta.getSaldo() - valor);
        contaRepository.save(conta);

        Transacao transacao = new Transacao(valor, "Saque", conta, null);
        transacaoRepository.save(transacao);
    }

    // ... (Outros métodos para transferência, pagamento, extrato, etc.)
}
