package com.example.demo.elevator.controller.api;

import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.Elevator;
import com.example.demo.elevator.model.UpdateElevator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/elevator")
public interface ElevatorController {

    @PostMapping
    ResponseEntity<Void> pickup(@RequestBody Call call);

    @PutMapping
    ResponseEntity<Elevator> updateCall(@RequestBody UpdateElevator newData);

    @PutMapping("/step")
    ResponseEntity<Void> step();

    @GetMapping
    ResponseEntity<List<Elevator>> status();

}
