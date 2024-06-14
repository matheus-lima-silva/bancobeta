package com.bancobeta.bancobeta.controller;


import com.bancobeta.bancobeta.model.entity.Conta;
import com.bancobeta.bancobeta.service.ContaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {


    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<Conta> criarConta(@Valid @RequestBody Conta conta) {
        Conta novaConta = contaService.criarConta(conta);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<Conta> obterContaPorNumero(@PathVariable String numeroConta) {
        Conta conta = contaService.obterContaPorNumero(numeroConta);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Conta>> obterContasPorCliente(@PathVariable Long clienteId) {
        List<Conta> contas = contaService.obterContasPorCliente(clienteId);
        return ResponseEntity.ok(contas);
    }

    @GetMapping("/{numeroConta}/saldo")
    public ResponseEntity<Double> consultarSaldo(@PathVariable String numeroConta) {
        double saldo = contaService.consultarSaldo(numeroConta);
        return ResponseEntity.ok(saldo);
    }

    @PostMapping("/{numeroConta}/deposito")
    public ResponseEntity<Void> depositar(@PathVariable String numeroConta, @RequestParam double valor) {
        contaService.depositar(numeroConta, valor);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{numeroConta}/saque")
    public ResponseEntity<Void> sacar(@PathVariable String numeroConta, @RequestParam double valor) {
        contaService.sacar(numeroConta, valor);
        return ResponseEntity.ok().build();
    }

}
