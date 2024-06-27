package com.bancobeta.bancobeta.service;


import com.bancobeta.bancobeta.exception.ExcecoesBancarias.ContaInvalidaException;
import com.bancobeta.bancobeta.exception.ExcecoesBancarias.SaldoInsuficienteException;
import com.bancobeta.bancobeta.model.entity.Conta;
import com.bancobeta.bancobeta.model.entity.Transacao;
import com.bancobeta.bancobeta.repository.ContaRepository;
import com.bancobeta.bancobeta.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransacaoService {


    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;

    public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
    }

    public Transacao realizarTransferencia(String numeroContaOrigem, String numeroContaDestino, double valor) {
        Conta contaOrigem = contaRepository.findByNumeroConta(numeroContaOrigem)
                .orElseThrow(() -> new ContaInvalidaException("Conta de origem não encontrada."));
        Conta contaDestino = contaRepository.findByNumeroConta(numeroContaDestino)
                .orElseThrow(() -> new ContaInvalidaException("Conta de destino não encontrada."));

        if (valor <= 0) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo.");
        }

        if (contaOrigem.getSaldo() < valor) {
            throw new SaldoInsuficienteException("Saldo insuficiente na conta de origem.");
        }

        contaOrigem.setSaldo(contaOrigem.getSaldo() - valor);
        contaDestino.setSaldo(contaDestino.getSaldo() + valor);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacao = new Transacao(valor, "Transferência", contaOrigem, contaDestino);
        return transacaoRepository.save(transacao);
    }

    public List<Transacao> listarTransacoesPorConta(String numeroConta) {
        Conta conta = contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ContaInvalidaException("Conta não encontrada."));
        return transacaoRepository.findByContaOrigemOrContaDestinoOrderByDataHoraDesc(conta, conta);
    }


}
