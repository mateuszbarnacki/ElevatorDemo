package com.example.demo.elevator.model;

import com.example.demo.elevator.common.Direction;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElevatorManagement {
    private final Elevator elevator;
    private final LinkedList<Call> elevatorCalls;
    private final LinkedList<Integer> currentRoute;
    private final Map<Direction, Comparator<Integer>> comparatorProvider;
    private final Map<Direction, Predicate<Call>> directionFilterProvider = ImmutableMap.<Direction, Predicate<Call>>builder()
            .put(Direction.GO_UP, call -> call.getTargetLevel() > call.getLevel())
            .put(Direction.GO_DOWN, call -> call.getTargetLevel() < call.getLevel())
            .build();

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
                this.currentRoute.addAll(calculateOptimalRoute(call));
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

    public void addCallToCurrentRoute(Call call) {
        if (!this.currentRoute.contains(call.getLevel())) {
            this.currentRoute.addFirst(call.getLevel());
        }
        if (!this.currentRoute.contains(call.getTargetLevel())) {
            this.currentRoute.addFirst(call.getTargetLevel());
        }
    }

    private List<Integer> calculateOptimalRoute(Call firstCall) {
        return firstCall.getTargetLevel() > firstCall.getLevel() ?
                calculateOptimalRouteUp(firstCall) : calculateOptimalRouteDown(firstCall);
    }

    private List<Integer> calculateOptimalRouteDown(Call firstCall) {
        List<Call> copy = new ArrayList<>(this.elevatorCalls);
        List<Call> additionalStops = copy.stream()
                .filter(directionFilterProvider.get(Direction.GO_DOWN))
                .filter(call -> call.getLevel() <= firstCall.getLevel())
                .filter(call -> call.getTargetLevel() >= firstCall.getTargetLevel())
                .collect(Collectors.toList());

        this.elevatorCalls.removeAll(additionalStops);

        return additionalStops.stream()
                .flatMap(call -> Stream.of(call.getLevel(), call.getTargetLevel()))
                .distinct()
                .sorted(this.comparatorProvider.get(Direction.GO_DOWN))
                .collect(Collectors.toList());
    }

    private List<Integer> calculateOptimalRouteUp(Call firstCall) {
        List<Call> copy = new ArrayList<>(this.elevatorCalls);
        List<Call> additionalStops = copy.stream()
                .filter(directionFilterProvider.get(Direction.GO_UP))
                .filter(call -> call.getLevel() >= firstCall.getLevel())
                .filter(call -> call.getTargetLevel() <= firstCall.getTargetLevel())
                .collect(Collectors.toList());

        this.elevatorCalls.removeAll(additionalStops);

        return additionalStops.stream()
                .flatMap(call -> Stream.of(call.getLevel(), call.getTargetLevel()))
                .distinct()
                .sorted(this.comparatorProvider.get(Direction.GO_UP))
                .collect(Collectors.toList());
    }
}
