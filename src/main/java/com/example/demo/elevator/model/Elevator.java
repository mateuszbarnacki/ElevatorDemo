package com.example.demo.elevator.model;

import com.example.demo.elevator.common.Direction;
import lombok.ToString;

@ToString
public class Elevator {
    private final int id;
    private Direction currentDirection;
    private int currentLevel;
    private int targetLevel;

    public Elevator(int id) {
        this.id = id;
        this.currentDirection = Direction.STAY;
        this.currentLevel = 0;
        this.targetLevel = 0;
    }

    public int getId() {
        return this.id;
    }

    public Direction getCurrentDirection() {
        return this.currentDirection;
    }

    public void setCurrentDirection(Direction direction) {
        this.currentDirection = direction;
    }

    public int getCurrentLevel() {
        return this.currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getTargetLevel() {
        return this.targetLevel;
    }

    public void setTargetLevel(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    public void move() {
        if (this.currentLevel != this.targetLevel) {
            this.currentDirection = this.targetLevel > this.currentLevel ? Direction.GO_UP : Direction.GO_DOWN;
            this.currentLevel += this.currentDirection.getValue();
        } else {
            this.currentDirection = Direction.STAY;
        }
    }

}
