package com.example.demo.elevator.service.impl;

import com.example.demo.elevator.common.Direction;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.Elevator;
import com.example.demo.elevator.model.ElevatorManagement;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.store.DataStore;
import com.example.demo.elevator.service.api.ElevatorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private final Map<Integer, ElevatorManagement> elevatorMap = DataStore.getInstance();

    @Override
    public void pickup(Call call) {
        validateCall(call);
        int elevatorId = call.getElevatorId();
        elevatorMap.get(elevatorId).addElevatorCall(call);
    }

    @Override
    public Elevator update(UpdateElevator updateData) {
        validateDataToUpdate(updateData);
        int elevatorId = updateData.getId();
        Elevator toUpdate = elevatorMap.get(elevatorId).getElevator();
        toUpdate.setCurrentLevel(updateData.getCurrentLevel());
        toUpdate.setTargetLevel(updateData.getTargetLevel());
        return elevatorMap.get(elevatorId)
                .getElevator();
    }

    @Override
    public void step() {
        elevatorMap.values().forEach(ElevatorManagement::move);
    }

    @Override
    public List<Elevator> status() {
        return elevatorMap.values().stream()
                .map(ElevatorManagement::getElevator)
                .collect(Collectors.toList());
    }


    private void validateCall(Call callDTO) {
        if (callDTO.getLevel() == callDTO.getTargetLevel()) {
            throw new RuntimeException("Target level should not be the same as level!");
        }
        if (callDTO.getElevatorId() < 1 || callDTO.getElevatorId() > elevatorMap.size()) {
            throw new RuntimeException("Called elevator does not exists!");
        }
    }

    private void validateDataToUpdate(UpdateElevator updateElevator) {
        if (updateElevator.getId() < 1 || updateElevator.getId() > elevatorMap.size()) {
            throw new RuntimeException("Could not update not existent elevator!");
        }
    }

    private Direction getCallDirection(Call callDTO) {
        return callDTO.getTargetLevel() > callDTO.getLevel() ? Direction.GO_UP : Direction.GO_DOWN;
    }

    private boolean shouldElevatorStop(Call call, Elevator elevator) {
        Direction callDirection = getCallDirection(call);
        return shouldElevatorStopWhileGoingDown(call, elevator, callDirection) ||
                shouldElevatorStopWhileGoingUp(call, elevator, callDirection);
    }

    private boolean shouldElevatorStopWhileGoingDown(Call call, Elevator elevator, Direction callDirection) {
        return Direction.GO_DOWN.equals(callDirection) &&
                elevator.getCurrentDirection().equals(callDirection) &&
                call.getLevel() < elevator.getCurrentLevel() &&
                call.getTargetLevel() >= elevator.getTargetLevel();
    }

    private boolean shouldElevatorStopWhileGoingUp(Call call, Elevator elevator, Direction callDirection) {
        return Direction.GO_UP.equals(callDirection) &&
                elevator.getCurrentDirection().equals(callDirection) &&
                call.getLevel() > elevator.getCurrentLevel() &&
                call.getTargetLevel() <= elevator.getTargetLevel();
    }

}
