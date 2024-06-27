package com.bancobeta.bancobeta.service;

import com.bancobeta.bancobeta.exception.ExcecoesBancarias.ClienteNaoEncontradoException;
import com.bancobeta.bancobeta.model.entity.Cliente;
import com.bancobeta.bancobeta.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {


    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente cadastrarCliente(Cliente cliente) {
        validarCpf(cliente.getCpf());
        validarEmail(cliente.getEmail());

        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    public Cliente obterClientePorCpf(String cpf) {
        Optional<Cliente> clienteOptional = clienteRepository.findByCpf(cpf);
        return clienteOptional.orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com o CPF: " + cpf));
    }

    public Cliente atualizarCliente(String cpf, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(cliente.getId())
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com o ID: " + cliente.getId()));

        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setEmail(cliente.getEmail());
        clienteExistente.setEndereco(cliente.getEndereco());
        clienteExistente.setRendaSalarial(cliente.getRendaSalarial());

        return clienteRepository.save(clienteExistente);
    }

    public void excluirCliente(String cpf) {
        Cliente clienteExistente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado com o CPF: " + cpf));
        clienteRepository.delete(clienteExistente);
    }

    private void validarCpf(String cpf) {
        // Validação rigorosa de CPF (lógica completa não incluída por brevidade)
        // ...
        if (clienteRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }
    }

    private void validarEmail(String email) {
        // Validação de email (lógica completa não incluída por brevidade)
        // ...
    }
}
