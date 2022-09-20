package com.example.demo.elevator.controller.api;

import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.Elevator;
import com.example.demo.elevator.model.UpdateElevator;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/elevator")
public interface ElevatorController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Make a call for the selected elevator",
            notes = "Method consumes JSON which contains selected elevator id, passenger level and target level.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Call added successfully"),
            @ApiResponse(code = 400, message = "Invalid call"),
            @ApiResponse(code = 500, message = "Server crashed")})
    ResponseEntity<Void> pickup(@RequestBody Call call);

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Update elevator state",
            notes = "Method change the current elevator level and the target elevator level." +
                    "Current elevator route is deleted.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Elevator updated successfully"),
            @ApiResponse(code = 400, message = "Invalid JSON"),
            @ApiResponse(code = 500, message = "Server crashed")})
    ResponseEntity<Elevator> updateElevatorState(@RequestBody UpdateElevator newData);

    @PutMapping("/step")
    @ApiOperation(value = "Perform simulation step", notes = "Method moves each elevator")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Simulation step preformed successfully"),
            @ApiResponse(code = 500, message = "Server crashed")})
    ResponseEntity<Void> step();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Fetch the status",
            notes = "Methods return the status of each elevator and collects them in one list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Status returned successfully"),
            @ApiResponse(code = 500, message = "Server crashed")})
    ResponseEntity<List<Elevator>> status();

}
