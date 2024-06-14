package com.bancobeta.bancobeta.config;


import com.bancobeta.bancobeta.model.entity.Cliente;
import com.bancobeta.bancobeta.model.entity.ContaCorrente;
import com.bancobeta.bancobeta.model.entity.ContaPagamento;
import com.bancobeta.bancobeta.repository.ClienteRepository;
import com.bancobeta.bancobeta.repository.ContaRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseInitializer implements CommandLineRunner {


    private final ClienteRepository clienteRepository;
    private final ContaRepository contaRepository;

    public DatabaseInitializer(ClienteRepository clienteRepository, ContaRepository contaRepository) {
        this.clienteRepository = clienteRepository;
        this.contaRepository = contaRepository;
    }

    @Override
    public void run(String... args) {
        if (clienteRepository.count() == 0) {
            Cliente cliente1 = new Cliente("João Silva", "17163934777", "joao@email.com", "Rua A, 123", 5000.0);
            Cliente cliente2 = new Cliente("Maria Souza", "89884361720", "maria@email.com", "Rua B, 456", 3000.0);

            clienteRepository.saveAll(Arrays.asList(cliente1, cliente2));

            ContaCorrente contaCorrente1 = new ContaCorrente("12345-6", 1000.0, cliente1, 500.0);
            ContaPagamento contaPagamento2 = new ContaPagamento("98765-4", 500.0, cliente2);

            contaRepository.saveAll(Arrays.asList(contaCorrente1, contaPagamento2));
        }
    }
}
