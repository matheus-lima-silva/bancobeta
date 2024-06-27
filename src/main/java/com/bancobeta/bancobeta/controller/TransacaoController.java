package com.bancobeta.bancobeta.controller;

import com.bancobeta.bancobeta.exception.ExcecoesBancarias.ContaInvalidaException;
import com.bancobeta.bancobeta.exception.ExcecoesBancarias.SaldoInsuficienteException;
import com.bancobeta.bancobeta.model.entity.Transacao;
import com.bancobeta.bancobeta.service.TransacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {


    private final TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(
            @RequestParam String numeroContaOrigem,
            @RequestParam String numeroContaDestino,
            @RequestParam double valor) {

        try {
            Transacao transacao = transacaoService.realizarTransferencia(numeroContaOrigem, numeroContaDestino, valor);
            return ResponseEntity.status(HttpStatus.CREATED).body(transacao.toString());
        } catch (SaldoInsuficienteException | ContaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/conta/{numeroConta}")
    public ResponseEntity<List<Transacao>> listarTransacoesPorConta(@PathVariable String numeroConta) {
        try {
            List<Transacao> transacoes = transacaoService.listarTransacoesPorConta(numeroConta);
            return ResponseEntity.ok(transacoes);
        } catch (ContaInvalidaException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
