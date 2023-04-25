package com.example.threads.problems;

import com.example.threads.controller.PedidoController;
import com.example.threads.model.Pedido;
import com.example.threads.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ConcurrencyTest {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoController pedidoController;

    @Test
    public void testConcorrenciaAoAtualizarPedido() throws InterruptedException {
        // cria um pedido inicial
        Pedido pedido = new Pedido();
        pedido.setDescricao("Pedido inicial");
        pedido.setValor(BigDecimal.TEN);
        pedido.setDataCriacao(LocalDateTime.now());
        pedidoRepository.save(pedido);

        // atualiza o pedido por duas threads diferentes
        CountDownLatch latch = new CountDownLatch(2);
        AtomicReference<Pedido> pedidoAtualizado1 = new AtomicReference<>();
        AtomicReference<Pedido> pedidoAtualizado2 = new AtomicReference<>();

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            try {
                Pedido pedido1 = pedidoController.atualizarPedido(pedido.getId(), new Pedido("Pedido atualizado 1", BigDecimal.ONE)).getBody();
                pedidoAtualizado1.set(pedido1);
            } finally {
                latch.countDown();
            }
        });
        executorService.submit(() -> {
            try {
                Pedido pedido2 = pedidoController.atualizarPedido(pedido.getId(), new Pedido("Pedido atualizado 2", BigDecimal.TEN)).getBody();
                pedidoAtualizado2.set(pedido2);
            } finally {
                latch.countDown();
            }
        });

        // espera as threads terminarem
        latch.await();

        // verifica que apenas um dos pedidos foi atualizado com sucesso
        Optional<Pedido> pedidoFinal = pedidoRepository.findById(pedido.getId());
        assertTrue(pedidoFinal.isPresent());
        if (pedidoAtualizado1.get() != null) {
            assertEquals("Pedido atualizado 1", pedidoFinal.get().getDescricao());
            assertEquals(0 , pedidoFinal.get().getValor().compareTo(BigDecimal.ONE));
            assertNull(pedidoAtualizado2.get());
        } else if (pedidoAtualizado2.get() != null) {
            assertEquals("Pedido atualizado 2", pedidoFinal.get().getDescricao());
            assertEquals(0, pedidoFinal.get().getValor().compareTo(BigDecimal.TEN));
            assertNull(pedidoAtualizado1.get());
        } else {
            fail("Um dos pedidos deveria ter sido atualizado com sucesso");
        }
    }
}