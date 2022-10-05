package com.example.demo.elevator.service.impl;

import com.example.demo.elevator.exception.InvalidLevelException;
import com.example.demo.elevator.exception.NotExistentElevatorException;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.ElevatorData;
import com.example.demo.elevator.model.api.ElevatorManagement;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.store.DataStore;
import com.example.demo.elevator.service.api.ElevatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ElevatorServiceImpl implements ElevatorService {
    private final Map<Integer, ElevatorManagement> elevatorMap = DataStore.getInstance();
    private final Logger logger = LoggerFactory.getLogger(ElevatorServiceImpl.class);

    @Override
    public void pickup(Call call) {
        validateCall(call);
        int elevatorId = call.getElevatorId();
        if (shouldElevatorStopForPassenger(call)) {
            elevatorMap.get(elevatorId).addCallToCurrentRoute(call);
        } else {
            elevatorMap.get(elevatorId).addElevatorCall(call);
        }
    }

    @Override
    public ElevatorData update(UpdateElevator updateData) {
        validateDataToUpdate(updateData);
        int elevatorId = updateData.getId();
        return elevatorMap.get(elevatorId)
                .updateElevator(updateData);
    }

    @Override
    public void step() {
        elevatorMap.values().forEach(ElevatorManagement::move);
    }

    @Override
    public List<ElevatorData> status() {
        return elevatorMap.values().stream()
                .map(ElevatorManagement::getElevatorData)
                .collect(Collectors.toList());
    }

    private void validateCall(Call callDTO) {
        if (callDTO.getLevel() == callDTO.getTargetLevel()) {
            logger.error("Target level should not be the same as level!");
            throw new InvalidLevelException("Target level should not be the same as level!");
        }
        if (callDTO.getElevatorId() < 1 || callDTO.getElevatorId() > elevatorMap.size()) {
            logger.error("Called elevator does not exists!");
            throw new NotExistentElevatorException("Called elevator does not exists!");
        }
    }

    private void validateDataToUpdate(UpdateElevator updateElevator) {
        if (updateElevator.getId() < 1 || updateElevator.getId() > elevatorMap.size()) {
            logger.error("Could not update not existent elevator!");
            throw new NotExistentElevatorException("Could not update not existent elevator!");
        }
    }

    private boolean shouldElevatorStopForPassenger(Call call) {
        return elevatorMap.get(call.getElevatorId()).isRouteExist() &&
                (shouldElevatorStopWhileGoingDown(call) || shouldElevatorStopWhileGoingUp(call));
    }

    private boolean shouldElevatorStopWhileGoingDown(Call call) {
        return call.getTargetLevel() < call.getLevel() &&
                call.getLevel() < elevatorMap.get(call.getElevatorId()).getCurrentElevatorLevel() &&
                call.getTargetLevel() >= elevatorMap.get(call.getElevatorId()).getRouteTargetLevel();
    }

    private boolean shouldElevatorStopWhileGoingUp(Call call) {
        return call.getTargetLevel() > call.getLevel() &&
                call.getLevel() > elevatorMap.get(call.getElevatorId()).getCurrentElevatorLevel() &&
                call.getTargetLevel() <= elevatorMap.get(call.getElevatorId()).getRouteTargetLevel();
    }

}
