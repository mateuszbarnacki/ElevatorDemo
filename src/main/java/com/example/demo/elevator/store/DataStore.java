package com.example.demo.elevator.store;

import com.example.demo.elevator.model.api.ElevatorManagement;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private static final Map<Integer, ElevatorManagement> instance = new HashMap<>();

    private DataStore() {}

    public static Map<Integer, ElevatorManagement> getInstance() {
        return instance;
    }

}
