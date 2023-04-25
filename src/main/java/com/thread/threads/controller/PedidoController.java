package com.thread.threads.controller;

import com.thread.threads.model.Pedido;
import com.thread.threads.repository.PedidoRepository;
import com.thread.threads.service.MyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private final PedidoRepository pedidoRepository;

    @Autowired
    private final MyService myService;

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

    /*@Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> atualizarPedido(Long id, Pedido pedidoAtualizado) {
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        if (pedido.isPresent()) {
            Pedido pedidoExistente = pedido.get();
            //entityManager.lock(pedidoExistente, LockModeType.PESSIMISTIC_WRITE);
            pedidoExistente.setDescricao(pedidoAtualizado.getDescricao());
            pedidoExistente.setValor(pedidoAtualizado.getValor());
            pedidoExistente.setDataCriacao(LocalDateTime.now());
            return ResponseEntity.ok(pedidoRepository.save(pedidoExistente));
        } else {
            System.out.println("Conflito de atualizacao");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
*/
    @GetMapping("/test")
    public String test() throws Exception{
        Thread.sleep(1000);
        return "finish";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscaPedido(@PathVariable Long id) {
        return pedidoRepository.findById(id).map(ResponseEntity::ok).orElseGet(() ->
                ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping("/service")
    public ResponseEntity<String> service() {
        myService.process();
        return ResponseEntity.ok("Sucesso");
    }
}
