package com.example.demo.model;

import com.example.demo.elevator.common.Direction;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.model.api.ElevatorManagement;
import com.example.demo.elevator.model.impl.ElevatorManagementImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ElevatorManagementImplTest {
    private final ElevatorManagement elevatorManagement = new ElevatorManagementImpl(1);

    @Test
    public void shouldElevatorPickupPassengerFromFourthLevelAndGoUpToThirteenLevel() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call call = new Call(1, 4, 13);
        elevatorManagement.addElevatorCall(call);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        Assert.assertEquals(call.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        Assert.assertEquals(call.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void shouldElevatorPickupPassengerFromEightLevelAndGoDownToSecondLevel() {
        UpdateElevator updateElevator = new UpdateElevator(1, 8, 8);
        elevatorManagement.updateElevator(updateElevator);

        Call call = new Call(1, 8, 2);
        elevatorManagement.addElevatorCall(call);

        elevatorManagement.move();
        // Elevator doesn't move, because it is on the right floor
        Assert.assertEquals(call.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }

        Assert.assertEquals(call.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void whenElevatorHasTwoCalls_thenPickupAllPassengersInSequentialOrder() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 2, 8);
        Call secondCall = new Call(1, 6, 10);
        elevatorManagement.addElevatorCall(firstCall);
        elevatorManagement.addElevatorCall(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void whenElevatorReceiveCallDuringRun_thenPickupAllPassengersInSequentialOrder() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 12, 5);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        Call secondCall = new Call(1, 7, 1);
        elevatorManagement.addElevatorCall(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoUp_whenAddCallToCurrentRoute_thenStopAndPickUpThePassenger() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 2, 8);
        Call secondCall = new Call(1, 4, 7);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            if (elevatorManagement.getCurrentElevatorLevel() == 3) {
                elevatorManagement.addCallToCurrentRoute(secondCall);
            }
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoDown_whenAddCallToCurrentRoute_thenStopAndPickUpThePassenger() {
        UpdateElevator updateElevator = new UpdateElevator(1, 10, 10);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 10, 2);
        Call secondCall = new Call(1, 7, 5);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            if (elevatorManagement.getCurrentElevatorLevel() == 9) {
                elevatorManagement.addCallToCurrentRoute(secondCall);
            }
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoUp_whenAddCallToCurrentRouteWithSameTargetLevelAsElevatorTargetLevel_thenStopAndPickUpThePassenger() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 2, 7);
        Call secondCall = new Call(1, 4, 7);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            if (elevatorManagement.getCurrentElevatorLevel() == 3) {
                elevatorManagement.addCallToCurrentRoute(secondCall);
            }
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoUp_whenAddCallToCurrentRouteDuringStay_thenStopAndPickUpThePassenger() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 2, 7);
        Call secondCall = new Call(1, 3, 6);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());
        elevatorManagement.addCallToCurrentRoute(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoDown_whenAddCallToCurrentRouteDuringStay_thenStopAndPickUpThePassenger() {
        UpdateElevator updateElevator = new UpdateElevator(1, 13, 13);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 13, 4);
        Call secondCall = new Call(1, 9, 6);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());
        elevatorManagement.addCallToCurrentRoute(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoDown_whenAddCallToCurrentRouteDuringStayWithDuplicatedLevel_thenStopAndPickUpThePassenger() {
        UpdateElevator updateElevator = new UpdateElevator(1, 13, 13);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 13, 4);
        Call secondCall = new Call(1, 9, 4);
        elevatorManagement.addElevatorCall(firstCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());
        elevatorManagement.addCallToCurrentRoute(secondCall);

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoUp_whenMoreThanOneCallInElevatorCallsList_thenAlgorithmCalculatesOptimalRoute() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 2, 13);
        Call secondCall = new Call(1, 9, 4);
        Call thirdCall = new Call(1, 4, 6);
        Call fourthCall = new Call(1, 6, 9);
        Call fifthCall = new Call(1, 12, 10);

        elevatorManagement.addElevatorCall(firstCall);
        elevatorManagement.addElevatorCall(secondCall);
        elevatorManagement.addElevatorCall(thirdCall);
        elevatorManagement.addElevatorCall(fourthCall);
        elevatorManagement.addElevatorCall(fifthCall);
        // Optimal route should be: 2 -> 4 -> 6 -> 9 -> 13

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(thirdCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(thirdCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(fourthCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoDown_whenMoreThanOneCallInElevatorCallsList_thenAlgorithmCalculatesOptimalRoute() {
        UpdateElevator updateElevator = new UpdateElevator(1, 14, 14);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 11, 3);
        Call secondCall = new Call(1, 9, 4);
        Call thirdCall = new Call(1, 8, 3);
        Call fourthCall = new Call(1, 6, 9);
        Call fifthCall = new Call(1, 12, 10);

        elevatorManagement.addElevatorCall(firstCall);
        elevatorManagement.addElevatorCall(secondCall);
        elevatorManagement.addElevatorCall(thirdCall);
        elevatorManagement.addElevatorCall(fourthCall);
        elevatorManagement.addElevatorCall(fifthCall);
        // Optimal route should be: 11 -> 9 -> 8 -> 4 -> 3

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(thirdCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(secondCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoDown_whenCallsAreTheSame_thenAlgorithmCalculatesOptimalRoute() {
        UpdateElevator updateElevator = new UpdateElevator(1, 14, 14);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 11, 3);
        Call secondCall = new Call(1, 2, 4);
        Call thirdCall = new Call(1, 11, 3);
        Call fourthCall = new Call(1, 6, 9);
        Call fifthCall = new Call(1, 11, 3);

        elevatorManagement.addElevatorCall(firstCall);
        elevatorManagement.addElevatorCall(secondCall);
        elevatorManagement.addElevatorCall(thirdCall);
        elevatorManagement.addElevatorCall(fourthCall);
        elevatorManagement.addElevatorCall(fifthCall);
        // Optimal route should be: 11 -> 3

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(fifthCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }

    @Test
    public void elevatorGoUp_whenCallsAreTheSame_thenAlgorithmCalculatesOptimalRoute() {
        UpdateElevator updateElevator = new UpdateElevator(1, 0, 0);
        elevatorManagement.updateElevator(updateElevator);

        Call firstCall = new Call(1, 2, 13);
        Call secondCall = new Call(1, 9, 4);
        Call thirdCall = new Call(1, 2, 13);
        Call fourthCall = new Call(1, 13, 4);
        Call fifthCall = new Call(1, 2, 13);

        elevatorManagement.addElevatorCall(firstCall);
        elevatorManagement.addElevatorCall(secondCall);
        elevatorManagement.addElevatorCall(thirdCall);
        elevatorManagement.addElevatorCall(fourthCall);
        elevatorManagement.addElevatorCall(fifthCall);
        // Optimal route should be: 2 -> 13

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(firstCall.getLevel(), elevatorManagement.getCurrentElevatorLevel());

        elevatorManagement.move();
        while (!elevatorManagement.getCurrentElevatorDirection().equals(Direction.STAY)) {
            elevatorManagement.move();
        }
        Assert.assertEquals(fifthCall.getTargetLevel(), elevatorManagement.getCurrentElevatorLevel());
    }
}
