package com.example.demo.elevator.service.api;

import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.Elevator;
import com.example.demo.elevator.model.UpdateElevator;

import java.util.List;

public interface ElevatorService {

    /**
     * This method add the request to call elevator.
     *
     * @param call A new call to the elevator.
     */
    void pickup(Call call);


    /**
     * This method updates existing call.
     *
     * @param newData An object with updated parameters.
     * @return An object after update.
     */
    Elevator update(UpdateElevator newData);

    /**
     * This method perform a single step of the animation.
     */
    void step();

    /**
     * This method collect the state of each elevator.
     *
     * @return List which contains the state of each elevator.
     */
    List<Elevator> status();

}
