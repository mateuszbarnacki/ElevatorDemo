package com.example.demo.elevator.common;

public enum Direction {
    GO_UP(1),
    STAY(0),
    GO_DOWN(-1);

    Direction(int direction) {
        this.value = direction;
    }

    public int getValue() {
        return this.value;
    }

    private final int value;
}
