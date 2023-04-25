package com.thread.threads.problems;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemoryLeakTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private final int NUM_REQUESTS = 100;

    @Test
    public void testSucessEndpoint() {
        String response = restTemplate
                .getForObject("http://localhost:" + port + "/pedidos/service", String.class);

        Assert.assertEquals("Sucesso", response);
    }

    @Test
    public void testMemoryLeak() throws Exception {
        List<Long> responseTimes = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_REQUESTS);
        for (int i = 0; i < NUM_REQUESTS; i++) {
            executorService.submit(() -> {
                long startTime = System.currentTimeMillis();
                String response = restTemplate.getForObject("http://localhost:" + port + "/pedidos/service", String.class);
                Assert.assertEquals("Sucesso", response);
                long endTime = System.currentTimeMillis();
                responseTimes.add(endTime - startTime);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        double avgResponseTime = responseTimes.stream().mapToLong(Long::longValue).average().orElse(0.0);
        System.out.println("Average response time: " + avgResponseTime + " ms");
    }

}
