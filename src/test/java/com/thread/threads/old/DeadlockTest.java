package com.thread.threads.old;

import org.junit.jupiter.api.Test;

public class DeadlockTest {
    /**
     * Nesse exemplo, o teste cria duas threads que tentam adquirir dois locks em ordens diferentes.
     * A thread1 adquire o lock1 primeiro e em seguida tenta adquirir o lock2,
     * enquanto a thread2 adquire o lock2 primeiro e em seguida tenta adquirir o lock1.
     *
     * Como as duas threads estão bloqueadas esperando a liberação de um lock que a outra está segurando,
     * ocorre um deadlock e o programa fica travado.
     */

    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    @Test
    public void testDeadlock() throws InterruptedException {

        Thread thread1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println("Thread 1: acquired lock1");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock2) {
                    System.out.println("Thread 1: acquired lock2");
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println("Thread 2: acquired lock2");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (lock1) {
                    System.out.println("Thread 2: acquired lock1");
                }
            }
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
    }

}
