package com.example.demo.elevator.model.impl;

import com.example.demo.elevator.common.Direction;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.Elevator;
import com.example.demo.elevator.model.ElevatorData;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.model.api.ElevatorManagement;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ElevatorManagementImpl implements ElevatorManagement {
    private final Elevator elevator;
    private final Deque<Call> elevatorCalls;
    private final LinkedList<Integer> currentRoute;
    private final Map<Direction, Comparator<Integer>> comparatorProvider;

    public ElevatorManagementImpl(int id) {
        this.elevator = new Elevator(id);
        this.elevatorCalls = new LinkedList<>();
        this.currentRoute = new LinkedList<>();
        this.comparatorProvider = ImmutableMap.<Direction, Comparator<Integer>>builder()
                .put(Direction.GO_UP, Comparator.naturalOrder())
                .put(Direction.GO_DOWN, Comparator.reverseOrder())
                .build();
    }

    @Override
    public void move() {
        if (shouldCreateAnimation()) {
            Call firstCallFromQueue = this.elevatorCalls.pollFirst();
            Optional.ofNullable(firstCallFromQueue).ifPresent(this::createAnimation);
        } else if (shouldContinueAnimation()) {
            Integer nextTargetLevel = this.currentRoute.pollFirst();
            Optional.ofNullable(nextTargetLevel).ifPresent(this::performAnimation);
        } else {
            this.elevator.move();
        }
    }

    @Override
    public ElevatorData getElevatorData() {
        return ElevatorData.builder()
                .id(this.elevator.getId())
                .currentDirection(this.elevator.getCurrentDirection())
                .currentLevel(this.elevator.getCurrentLevel())
                .targetLevel(this.elevator.getTargetLevel())
                .build();
    }

    @Override
    public Direction getCurrentElevatorDirection() {
        return this.elevator.getCurrentDirection();
    }

    @Override
    public int getCurrentElevatorLevel() {
        return this.elevator.getCurrentLevel();
    }

    @Override
    public void addElevatorCall(Call call) {
        this.elevatorCalls.addLast(call);
    }

    @Override
    public ElevatorData updateElevator(UpdateElevator updateData) {
        modifyElevator(updateData);
        clearElevatorCallsAndRoute();
        return getElevatorData();
    }

    @Override
    public int getRouteTargetLevel() {
        return this.currentRoute.getLast();
    }

    @Override
    public boolean isRouteExist() {
        return !this.currentRoute.isEmpty();
    }

    @Override
    public void addCallToCurrentRoute(Call call) {
        if (!this.elevator.getCurrentDirection().equals(Direction.STAY)) {
            this.currentRoute.addFirst(this.elevator.getTargetLevel());
        }
        addCallLevels(call);
        Direction callDirection = call.getTargetLevel() > call.getLevel() ? Direction.GO_UP : Direction.GO_DOWN;
        this.currentRoute.sort(this.comparatorProvider.get(callDirection));
        this.elevator.setTargetLevel(this.currentRoute.getFirst());
        if (!this.elevator.getCurrentDirection().equals(Direction.STAY)) {
            this.currentRoute.removeFirst();
        }
    }

    private void modifyElevator(UpdateElevator updateData) {
        this.elevator.setCurrentLevel(updateData.getCurrentLevel());
        this.elevator.setTargetLevel(updateData.getTargetLevel());
        this.elevator.setCurrentDirection(Direction.STAY);
    }

    private void clearElevatorCallsAndRoute() {
        this.elevatorCalls.clear();
        this.currentRoute.clear();
    }

    private boolean shouldCreateAnimation() {
        return this.currentRoute.isEmpty() && this.elevator.getCurrentDirection().equals(Direction.STAY);
    }

    private boolean shouldContinueAnimation() {
        return !this.currentRoute.isEmpty() && this.elevator.getCurrentDirection().equals(Direction.STAY);
    }

    private void createAnimation(Call call) {
        this.currentRoute.addAll(calculateOptimalRoute(call));
        this.elevator.setTargetLevel(call.getLevel());
        this.currentRoute.removeIf(level -> level == call.getLevel());
        if (!this.currentRoute.contains(call.getTargetLevel())) {
            this.currentRoute.addLast(call.getTargetLevel());
        }
        this.elevator.move();
    }

    private void performAnimation(Integer nextTargetLevel) {
        this.elevator.setTargetLevel(nextTargetLevel);
        this.elevator.move();
    }

    private void addCallLevels(Call call) {
        if (!this.currentRoute.contains(call.getLevel())) {
            this.currentRoute.addLast(call.getLevel());
        }
        if (!this.currentRoute.contains(call.getTargetLevel())) {
            this.currentRoute.addLast(call.getTargetLevel());
        }
    }

    private List<Integer> calculateOptimalRoute(Call firstCall) {
        return firstCall.getTargetLevel() > firstCall.getLevel() ?
                calculateOptimalRouteUp(firstCall) : calculateOptimalRouteDown(firstCall);
    }

    private List<Integer> calculateOptimalRouteDown(Call firstCall) {
        List<Call> additionalStops = createAdditionalStopsForRouteDown(firstCall);
        this.elevatorCalls.removeAll(additionalStops);

        return additionalStops.stream()
                .flatMap(call -> Stream.of(call.getLevel(), call.getTargetLevel()))
                .distinct()
                .sorted(this.comparatorProvider.get(Direction.GO_DOWN))
                .collect(Collectors.toList());
    }

    private List<Call> createAdditionalStopsForRouteDown(Call firstCall) {
        List<Call> copy = new ArrayList<>(this.elevatorCalls);
        return copy.stream()
                .filter(call -> call.getTargetLevel() < call.getLevel())
                .filter(call -> call.getLevel() <= firstCall.getLevel())
                .filter(call -> call.getTargetLevel() >= firstCall.getTargetLevel())
                .collect(Collectors.toList());
    }

    private List<Integer> calculateOptimalRouteUp(Call firstCall) {
        List<Call> additionalStops = createAdditionalStopsForRouteUp(firstCall);
        this.elevatorCalls.removeAll(additionalStops);

        return additionalStops.stream()
                .flatMap(call -> Stream.of(call.getLevel(), call.getTargetLevel()))
                .distinct()
                .sorted(this.comparatorProvider.get(Direction.GO_UP))
                .collect(Collectors.toList());
    }

    private List<Call> createAdditionalStopsForRouteUp(Call firstCall) {
        List<Call> copy = new ArrayList<>(this.elevatorCalls);
        return copy.stream()
                .filter(call -> call.getTargetLevel() > call.getLevel())
                .filter(call -> call.getLevel() >= firstCall.getLevel())
                .filter(call -> call.getTargetLevel() <= firstCall.getTargetLevel())
                .collect(Collectors.toList());
    }
}
