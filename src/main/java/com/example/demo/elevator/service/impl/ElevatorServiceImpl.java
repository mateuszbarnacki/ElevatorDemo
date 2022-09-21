package com.example.demo.elevator.service.impl;

import com.example.demo.elevator.common.Direction;
import com.example.demo.elevator.exception.InvalidLevelException;
import com.example.demo.elevator.exception.NotExistentElevatorException;
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
        Elevator elevator = elevatorMap.get(elevatorId).getElevator();
        if (shouldElevatorStopForPassenger(call, elevator)) {
            elevatorMap.get(elevatorId).addCallToCurrentRoute(call);
        } else {
            elevatorMap.get(elevatorId).addElevatorCall(call);
        }
    }

    @Override
    public Elevator update(UpdateElevator updateData) {
        validateDataToUpdate(updateData);
        int elevatorId = updateData.getId();

        Elevator toUpdate = elevatorMap.get(elevatorId).getElevator();
        toUpdate.setCurrentLevel(updateData.getCurrentLevel());
        toUpdate.setTargetLevel(updateData.getTargetLevel());
        toUpdate.setCurrentDirection(Direction.STAY);
        elevatorMap.get(elevatorId).clearElevatorCallsAndRoute();

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
            throw new InvalidLevelException("Target level should not be the same as level!");
        }
        if (callDTO.getElevatorId() < 1 || callDTO.getElevatorId() > elevatorMap.size()) {
            throw new NotExistentElevatorException("Called elevator does not exists!");
        }
    }

    private void validateDataToUpdate(UpdateElevator updateElevator) {
        if (updateElevator.getId() < 1 || updateElevator.getId() > elevatorMap.size()) {
            throw new NotExistentElevatorException("Could not update not existent elevator!");
        }
    }

    private boolean shouldElevatorStopForPassenger(Call call, Elevator elevator) {
        return elevatorMap.get(elevator.getId()).isRouteExist() &&
                (shouldElevatorStopWhileGoingDown(call, elevator) || shouldElevatorStopWhileGoingUp(call, elevator));
    }

    private boolean shouldElevatorStopWhileGoingDown(Call call, Elevator elevator) {
        return call.getTargetLevel() < call.getLevel() &&
                call.getLevel() < elevator.getCurrentLevel() &&
                call.getTargetLevel() >= elevatorMap.get(elevator.getId()).getRouteTargetLevel();
    }

    private boolean shouldElevatorStopWhileGoingUp(Call call, Elevator elevator) {
        return call.getTargetLevel() > call.getLevel() &&
                call.getLevel() > elevator.getCurrentLevel() &&
                call.getTargetLevel() <= elevatorMap.get(elevator.getId()).getRouteTargetLevel();
    }

}
