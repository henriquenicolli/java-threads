package com.thread.threads;

import com.thread.threads.controller.PedidoController;
import com.thread.threads.model.Pedido;
import com.thread.threads.repository.PedidoRepository;
import jakarta.transaction.Transactional;
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
public class PedidoControllerTest {

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

        // recupera o pedido criado
        Optional<Pedido> pedidoInicial = pedidoRepository.findById(pedido.getId());
        assertTrue(pedidoInicial.isPresent());

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