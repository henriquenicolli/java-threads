package com.thread.threads.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyService {

    private List<byte[]> list = new ArrayList<>();

    @Async
    public void process() {
        for (int i = 0; i < 1000; i++) {
            byte[] bytes = new byte[1024 * 1024];
            list.add(bytes);
        }
    }
}