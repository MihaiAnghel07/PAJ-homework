package com.luxoft.bankapp.domain;

import java.util.ArrayList;
import java.util.List;

public class Queue {

    private final List<Email> queue;

    private Queue() {
        queue = new ArrayList<>();
    }

    public void add(Email email) {
        queue.add(email);
    }

    public Email retrieve() {
        if (!queue.isEmpty())
            return queue.remove(0);

        return null;
    }

    private static class SingletonHelper {
        private static final Queue INSTANCE = new Queue();
    }

    public static Queue getInstance() {
        return SingletonHelper.INSTANCE;
    }

}
