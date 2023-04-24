/*
package com.thread.threads;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadPoolDatabaseTest {

    @Test
    public void testThreadPoolPerformance() {
        int numExecutions = 10;

        long singleThreadTime = 0;
        for (int i = 0; i < numExecutions; i++) {
            long startTime = System.currentTimeMillis();
            int sum = new SingleThreadQueryExecutor().execute();
            singleThreadTime += System.currentTimeMillis() - startTime;
        }
        singleThreadTime /= numExecutions;

        long threadPoolTime = 0;
        for (int i = 0; i < numExecutions; i++) {
            long startTime = System.currentTimeMillis();
            int poolSize = 4;
            ExecutorService executor = Executors.newFixedThreadPool(poolSize);
            int sum = 0;
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD)) {
                int numRecords = getNumRecords(conn, "users");
                for (int j = 0; j < poolSize; j++) {
                    int fromIndex = j * (numRecords / poolSize);
                    int toIndex = (j + 1) * (numRecords / poolSize);
                    executor.submit(new ThreadPoolQueryExecutor(conn, fromIndex, toIndex));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            executor.shutdown();
            while (!executor.isTerminated()) {}
            threadPoolTime += System.currentTimeMillis() - startTime;
        }

        assertTrue(threadPoolTime < singleThreadTime * 0.5);
    }
}
*/
