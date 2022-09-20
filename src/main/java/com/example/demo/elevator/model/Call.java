package com.example.demo.elevator.model;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Value;

@Value
@NonNull
@EqualsAndHashCode
public class Call {
    int elevatorId;
    int level;
    int targetLevel;
}
