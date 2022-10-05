package com.example.demo.elevator.model.api;

import com.example.demo.elevator.common.Direction;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.ElevatorData;
import com.example.demo.elevator.model.UpdateElevator;

public interface ElevatorManagement {

    void move();

    void addCallToCurrentRoute(Call call);

    void addElevatorCall(Call call);

    ElevatorData updateElevator(UpdateElevator updateElevator);

    ElevatorData getElevatorData();

    boolean isRouteExist();

    int getCurrentElevatorLevel();

    int getRouteTargetLevel();

    Direction getCurrentElevatorDirection();
}
