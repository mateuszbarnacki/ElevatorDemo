package com.example.demo.elevator.model;

import lombok.NonNull;
import lombok.Value;

import java.beans.ConstructorProperties;

@Value
@NonNull
public class UpdateElevator {
    int id;
    int currentLevel;
    int targetLevel;

    @ConstructorProperties({"id", "currentLevel", "targetLevel"})
    public UpdateElevator(int id, int currentLevel, int targetLevel) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.targetLevel = targetLevel;
    }
}
