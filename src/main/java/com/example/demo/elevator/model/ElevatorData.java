package com.example.demo.elevator.model;

import com.example.demo.elevator.common.Direction;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ElevatorData {
    int id;
    @Builder.Default
    Direction currentDirection = Direction.STAY;
    @Builder.Default
    int currentLevel = 0;
    @Builder.Default
    int targetLevel = 0;
}
