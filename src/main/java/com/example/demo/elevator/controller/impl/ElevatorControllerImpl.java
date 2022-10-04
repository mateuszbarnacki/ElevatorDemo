package com.example.demo.elevator.controller.impl;

import com.example.demo.elevator.controller.api.ElevatorController;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.ElevatorData;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.service.api.ElevatorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class ElevatorControllerImpl implements ElevatorController {
    private final ElevatorService service;

    @Override
    public ResponseEntity<Void> pickup(Call call) {
        service.pickup(call);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<ElevatorData> updateElevatorState(UpdateElevator newData) {
        return ResponseEntity.ok(service.update(newData));
    }

    @Override
    public ResponseEntity<Void> step() {
        service.step();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<List<ElevatorData>> status() {
        return ResponseEntity.ok(service.status());
    }

}
