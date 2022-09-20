package com.example.demo.elevator.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

import java.beans.ConstructorProperties;

@Value
@NonNull
@EqualsAndHashCode
public class Call {
    int elevatorId;
    int level;
    int targetLevel;

    @ConstructorProperties({"elevatorId", "level", "targetLevel"})
    public Call(int elevatorId, int level, int targetLevel) {
        this.elevatorId = elevatorId;
        this.level = level;
        this.targetLevel = targetLevel;
    }
}
