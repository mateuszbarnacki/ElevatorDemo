package com.example.demo.model;

import com.example.demo.elevator.common.Direction;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.Elevator;
import com.example.demo.elevator.model.ElevatorManagement;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ElevatorManagementTest {
    private final ElevatorManagement elevatorManagement = new ElevatorManagement(1);

    @Test
    public void shouldReturnElevatorWithIdEqualOne() {
        Elevator elevator = elevatorManagement.getElevator();
        AssertionsForClassTypes.assertThat(elevator)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 0)
                .hasFieldOrPropertyWithValue("targetLevel", 0);
    }

    @Test
    public void shouldElevatorPickupPassengerFromFourthLevelAndGoUpToThirteenLevel() {
        elevatorManagement.getElevator().setCurrentLevel(0);
        elevatorManagement.getElevator().setTargetLevel(0);

        Call call = new Call(1, 4, 13);
        elevatorManagement.addElevatorCall(call);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 4)
                .hasFieldOrPropertyWithValue("targetLevel", 4);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 13)
                .hasFieldOrPropertyWithValue("targetLevel", 13);
    }

    @Test
    public void shouldElevatorPickupPassengerFromEightLevelAndGoDownToSecondLevel() {
        elevatorManagement.getElevator().setCurrentLevel(8);
        elevatorManagement.getElevator().setTargetLevel(8);

        Call call = new Call(1, 8, 2);
        elevatorManagement.addElevatorCall(call);

        elevatorManagement.move();
        // Elevator doesn't move, because it is on the right floor
        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 8)
                .hasFieldOrPropertyWithValue("targetLevel", 8);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 2)
                .hasFieldOrPropertyWithValue("targetLevel", 2);
    }

    @Test
    public void whenElevatorHasTwoCalls_thenPickupAllPassengersInSequentialOrder() {
        elevatorManagement.getElevator().setCurrentLevel(0);
        elevatorManagement.getElevator().setTargetLevel(0);

        Call firstCall = new Call(1, 2, 8);
        Call secondCall = new Call(1, 6, 10);
        elevatorManagement.addElevatorCall(firstCall);
        elevatorManagement.addElevatorCall(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 2)
                .hasFieldOrPropertyWithValue("targetLevel", 2);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 8)
                .hasFieldOrPropertyWithValue("targetLevel", 8);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 6)
                .hasFieldOrPropertyWithValue("targetLevel", 6);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 10)
                .hasFieldOrPropertyWithValue("targetLevel", 10);
    }

    @Test
    public void whenElevatorReceiveCallDuringRun_thenPickupAllPassengersInSequentialOrder() {
        elevatorManagement.getElevator().setCurrentLevel(0);
        elevatorManagement.getElevator().setTargetLevel(0);

        Call firstCall = new Call(1, 12, 5);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 12)
                .hasFieldOrPropertyWithValue("targetLevel", 12);

        Call secondCall = new Call(1, 7, 1);
        elevatorManagement.addElevatorCall(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 5)
                .hasFieldOrPropertyWithValue("targetLevel", 5);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 7)
                .hasFieldOrPropertyWithValue("targetLevel", 7);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 1)
                .hasFieldOrPropertyWithValue("targetLevel", 1);
    }

    @Test
    public void elevatorGoUp_whenAddCallToCurrentRoute_thenStopAndPickUpThePassenger() {
        elevatorManagement.getElevator().setCurrentLevel(0);
        elevatorManagement.getElevator().setTargetLevel(0);

        Call firstCall = new Call(1, 2, 8);
        Call secondCall = new Call(1, 4, 7);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 2)
                .hasFieldOrPropertyWithValue("targetLevel", 2);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            if (elevatorManagement.getElevator().getCurrentLevel() == 3) {
                elevatorManagement.addCallToCurrentRoute(secondCall);
            }
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 4)
                .hasFieldOrPropertyWithValue("targetLevel", 4);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 7)
                .hasFieldOrPropertyWithValue("targetLevel", 7);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 8)
                .hasFieldOrPropertyWithValue("targetLevel", 8);
    }

    @Test
    public void elevatorGoDown_whenAddCallToCurrentRoute_thenStopAndPickUpThePassenger() {
        elevatorManagement.getElevator().setCurrentLevel(10);
        elevatorManagement.getElevator().setTargetLevel(10);

        Call firstCall = new Call(1, 10, 2);
        Call secondCall = new Call(1, 7, 5);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 10)
                .hasFieldOrPropertyWithValue("targetLevel", 10);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            if (elevatorManagement.getElevator().getCurrentLevel() == 9) {
                elevatorManagement.addCallToCurrentRoute(secondCall);
            }
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 7)
                .hasFieldOrPropertyWithValue("targetLevel", 7);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 5)
                .hasFieldOrPropertyWithValue("targetLevel", 5);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 2)
                .hasFieldOrPropertyWithValue("targetLevel", 2);
    }

    @Test
    public void elevatorGoUp_whenAddCallToCurrentRouteWithSameTargetLevelAsElevatorTargetLevel_thenStopAndPickUpThePassenger() {
        elevatorManagement.getElevator().setCurrentLevel(0);
        elevatorManagement.getElevator().setTargetLevel(0);

        Call firstCall = new Call(1, 2, 7);
        Call secondCall = new Call(1, 4, 7);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 2)
                .hasFieldOrPropertyWithValue("targetLevel", 2);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            if (elevatorManagement.getElevator().getCurrentLevel() == 3) {
                elevatorManagement.addCallToCurrentRoute(secondCall);
            }
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 4)
                .hasFieldOrPropertyWithValue("targetLevel", 4);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 7)
                .hasFieldOrPropertyWithValue("targetLevel", 7);
    }

    @Test
    public void elevatorGoUp_whenAddCallToCurrentRouteDuringStay_thenStopAndPickUpThePassenger() {
        elevatorManagement.getElevator().setCurrentLevel(0);
        elevatorManagement.getElevator().setTargetLevel(0);

        Call firstCall = new Call(1, 2, 7);
        Call secondCall = new Call(1, 3, 6);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 2)
                .hasFieldOrPropertyWithValue("targetLevel", 2);
        elevatorManagement.addCallToCurrentRoute(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 3)
                .hasFieldOrPropertyWithValue("targetLevel", 3);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 6)
                .hasFieldOrPropertyWithValue("targetLevel", 6);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 7)
                .hasFieldOrPropertyWithValue("targetLevel", 7);
    }

    @Test
    public void elevatorGoDown_whenAddCallToCurrentRouteDuringStay_thenStopAndPickUpThePassenger() {
        elevatorManagement.getElevator().setCurrentLevel(13);
        elevatorManagement.getElevator().setTargetLevel(13);

        Call firstCall = new Call(1, 13, 4);
        Call secondCall = new Call(1, 9, 6);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 13)
                .hasFieldOrPropertyWithValue("targetLevel", 13);
        elevatorManagement.addCallToCurrentRoute(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 9)
                .hasFieldOrPropertyWithValue("targetLevel", 9);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 6)
                .hasFieldOrPropertyWithValue("targetLevel", 6);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 4)
                .hasFieldOrPropertyWithValue("targetLevel", 4);
    }

    @Test
    public void elevatorGoDown_whenAddCallToCurrentRouteDuringStayWithDuplicatedLevel_thenStopAndPickUpThePassenger() {
        elevatorManagement.getElevator().setCurrentLevel(13);
        elevatorManagement.getElevator().setTargetLevel(13);

        Call firstCall = new Call(1, 13, 4);
        Call secondCall = new Call(1, 9, 4);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 13)
                .hasFieldOrPropertyWithValue("targetLevel", 13);
        elevatorManagement.addCallToCurrentRoute(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 9)
                .hasFieldOrPropertyWithValue("targetLevel", 9);

        elevatorManagement.move();
        while (!elevatorManagement.getElevator().getCurrentDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        AssertionsForClassTypes.assertThat(elevatorManagement.getElevator())
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("currentDirection", Direction.STAY)
                .hasFieldOrPropertyWithValue("currentLevel", 4)
                .hasFieldOrPropertyWithValue("targetLevel", 4);
    }
}
