package com.thread.threads;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MemoryLeakTest {

    /**
    * Nesse exemplo, o teste cria 1000 threads que alocam 1MB de
    * memória cada uma e adicionam o array resultante em uma lista.
    * Como a lista não é limpa, a cada iteração do loop, mais memória é alocada
    * e a lista cresce indefinidamente, causando um problema de memory leak.
    */

    @Test
    public void testMemoryLeak() throws InterruptedException {

        List<byte[]> list = new ArrayList<>();

        for (int i = 0; i < 10000; i++) {
            Thread thread = new Thread(() -> {
                byte[] bytes = new byte[1024 * 1024]; // aloca 1MB de memória
                list.add(bytes);
            });
            thread.start();
            thread.join(); // aguarda a thread terminar
        }

        System.out.println("List size: " + list.size());
    }

}
