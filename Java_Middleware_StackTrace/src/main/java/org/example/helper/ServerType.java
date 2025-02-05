package org.example.helper;

public enum ServerType {
    PAYLOAD(1),
    GRAPH(2),
    SQL(3),
    VIDEO(4);

    private final int value;

    ServerType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
