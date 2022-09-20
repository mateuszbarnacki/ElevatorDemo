package com.example.demo.elevator.model;

import com.example.demo.elevator.common.Direction;
import com.google.common.collect.ImmutableMap;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Map;

public class ElevatorManagement {
    private final Elevator elevator;
    private final LinkedList<Call> elevatorCalls;
    private final LinkedList<Integer> currentRoute;
    private final Map<Direction, Comparator<Integer>> comparatorProvider;

    public ElevatorManagement(int id) {
        this.elevator = new Elevator(id);
        this.elevatorCalls = new LinkedList<>();
        this.currentRoute = new LinkedList<>();
        this.comparatorProvider = ImmutableMap.<Direction, Comparator<Integer>>builder()
                .put(Direction.GO_UP, Comparator.naturalOrder())
                .put(Direction.GO_DOWN, Comparator.reverseOrder())
                .build();
    }

    public Elevator getElevator() {
        return this.elevator;
    }

    public void addElevatorCall(Call call) {
        this.elevatorCalls.addLast(call);
    }

    public void move() {
        if (this.currentRoute.isEmpty() && this.elevator.getCurrentDirection().equals(Direction.STAY)) {
            Call call = this.elevatorCalls.pollFirst();
            if (call != null) {
                this.elevator.setTargetLevel(call.getLevel());
                this.currentRoute.addLast(call.getTargetLevel());
            } else {
                return;
            }
        } else if (!this.currentRoute.isEmpty() && this.elevator.getCurrentDirection().equals(Direction.STAY)) {
            Integer nextTargetLevel = this.currentRoute.pollFirst();
            if (nextTargetLevel != null) {
                this.elevator.setTargetLevel(nextTargetLevel);
            } else {
                return;
            }
        }
        this.elevator.move();
    }
}
