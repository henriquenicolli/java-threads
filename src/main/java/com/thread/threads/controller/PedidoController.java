package com.thread.threads.controller;

import com.thread.threads.model.Pedido;
import com.thread.threads.repository.PedidoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private final PedidoRepository pedidoRepository;

    @Autowired
    private final EntityManager entityManager;

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(@PathVariable Long id, @RequestBody Pedido pedido) {
        Optional<Pedido> pedidoAtual = pedidoRepository.findById(id);
        if (pedidoAtual.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pedido pedidoAtualizado = pedidoAtual.get();
        pedidoAtualizado.setDescricao(pedido.getDescricao());
        pedidoAtualizado.setValor(pedido.getValor());

        try {
            Pedido pedidoSalvo = pedidoRepository.save(pedidoAtualizado);
            return ResponseEntity.ok(pedidoSalvo);
        } catch (OptimisticLockException ex) {
            System.out.println("Conflito de atualizacao");
           return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    /*@Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(Long id, Pedido pedidoAtualizado) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            Pedido pedidoExistente = pedido.get();
            entityManager.lock(pedidoExistente, LockModeType.PESSIMISTIC_WRITE);
            pedidoExistente.setDescricao(pedidoAtualizado.getDescricao());
            pedidoExistente.setValor(pedidoAtualizado.getValor());
            pedidoExistente.setDataCriacao(LocalDateTime.now());
            return ResponseEntity.ok(pedidoRepository.save(pedidoExistente));
        } else {
            System.out.println("Conflito de atualizacao");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }*/

}
