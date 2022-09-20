package com.example.demo;

import com.example.demo.elevator.model.ElevatorManagement;
import com.example.demo.elevator.store.DataStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        Logger logger = Logger.getGlobal();
        int numberOfElevators;

        if (args.length != 1) {
            logger.info(() -> "Could not run program: unknown number of elevators!");
            return;
        }

        try {
            numberOfElevators = Integer.parseInt(args[0]);
        } catch (NumberFormatException nfe) {
            logger.info(() -> "Could not run program. " + nfe.getMessage());
            return;
        }

        if (numberOfElevators < 1 || numberOfElevators > 16) {
            logger.info(() -> "Could not run program: invalid number of elevators!");
            return;
        }

        initializeElevatorRepository(numberOfElevators);

        SpringApplication.run(DemoApplication.class, args);
    }

    private static void initializeElevatorRepository(int numberOfElevators) {
        Map<Integer, ElevatorManagement> elevatorMap = DataStore.getInstance();
        IntStream.rangeClosed(1, numberOfElevators)
                .forEach(number -> elevatorMap.put(number, new ElevatorManagement(number)));
    }

}
