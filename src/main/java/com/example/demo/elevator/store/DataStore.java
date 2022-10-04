package com.example.demo.elevator.store;

import com.example.demo.elevator.model.impl.ElevatorManagementImpl;

import java.util.HashMap;
import java.util.Map;

public class DataStore {
    private static final Map<Integer, ElevatorManagementImpl> instance = new HashMap<>();

    private DataStore() {}

    public static Map<Integer, ElevatorManagementImpl> getInstance() {
        return instance;
    }

}
