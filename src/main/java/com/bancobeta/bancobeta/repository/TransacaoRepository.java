package com.bancobeta.bancobeta.repository;

import com.bancobeta.bancobeta.model.entity.Conta;
import com.bancobeta.bancobeta.model.entity.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, String> {


    List<Transacao> findByContaOrigem(Conta contaOrigem);
    List<Transacao> findByContaDestino(Conta contaDestino);

    List<Transacao> findByContaOrigemOrContaDestinoOrderByDataHoraDesc(Conta conta, Conta conta1);
}
