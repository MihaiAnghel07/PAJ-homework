package com.luxoft.bankapp.domain;

import java.util.Objects;

public class Email {
    private final Client client;
    private final String source;
    private final String destination;

    public Email(Client client, String source, String destination) {
        this.client = client;
        this.source = source;
        this.destination = destination;
    }

    public Client getClient() {
        return client;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Email email = (Email) o;
        return Objects.equals(client, email.client) &&
                Objects.equals(source, email.source) &&
                Objects.equals(destination, email.destination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, source, destination);
    }
}
