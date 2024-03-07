package com.luxoft.bankapp.service;

import com.luxoft.bankapp.domain.Email;
import com.luxoft.bankapp.domain.Queue;

public class EmailService {

    private static final Queue queue = Queue.getInstance();
    private static boolean isGoingToBeClosed = false;

    public EmailService() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isGoingToBeClosed) return;

                while (true) {
                    synchronized (queue) {
                        Email email = queue.retrieve();
                        if (email == null) {
                            try {
                                queue.wait();
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            System.out.println("Sent email to " + email.getDestination());
                        }
                    }

                    if (isGoingToBeClosed) return;
                }
            }
        }).start();
    }

    public static void sendNotificationEmail(Email email) {
        synchronized (queue) {
            if (!isGoingToBeClosed) {
                queue.add(email);
                queue.notify();
            } else {
                System.out.format("The app is going to be closed, can no longer send notification emails!%n%n");
            }
        }
    }

    public void close() {
        synchronized (queue) {
            isGoingToBeClosed = true;
            queue.notifyAll();
        }
    }
}
