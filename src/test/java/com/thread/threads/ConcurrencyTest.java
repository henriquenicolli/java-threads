package com.thread.threads;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConcurrencyTest {

	private int counter = 0;

	/***
	Nesse exemplo, o teste cria um pool de 100 threads que incrementam o valor de um contador 100 vezes cada uma.
	Como o método incrementCounter() é sincronizado, apenas uma thread pode acessá-lo por vez,
	evitando problemas de concorrência. Ao final do teste, o valor final do contador é impresso no console.

	Para simular um problema de concorrência, você pode remover a palavra-chave "synchronized"
	do método incrementCounter(). Nesse caso, várias threads tentarão acessar o
	método ao mesmo tempo, podendo gerar resultados incorretos.
	***/

	@Test
	public void testConcurrency() throws InterruptedException {

		int numThreads = 1000;
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		for (int i = 0; i < numThreads; i++) {
			executor.execute(() -> {
				for (int j = 0; j < numThreads; j++) {
					incrementCounter();
				}
			});
		}

		executor.shutdown();
		executor.awaitTermination(100, TimeUnit.MILLISECONDS);

		Assertions.assertEquals(numThreads * numThreads, counter);
		System.out.println("Counter value: " + counter);
	}

	private synchronized void incrementCounter() {
		counter++;
	}

}
