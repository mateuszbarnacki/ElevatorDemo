package com.example.demo.elevator.model;

import lombok.NonNull;
import lombok.Value;

@Value
@NonNull
public class UpdateElevator {
    int id;
    int currentLevel;
    int targetLevel;
}
