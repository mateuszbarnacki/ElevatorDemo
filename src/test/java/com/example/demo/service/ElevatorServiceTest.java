package com.example.demo.service;

import com.example.demo.elevator.exception.InvalidLevelException;
import com.example.demo.elevator.exception.NotExistentElevatorException;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.ElevatorData;
import com.example.demo.elevator.model.impl.ElevatorManagementImpl;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.service.api.ElevatorService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElevatorServiceTest {

    @Autowired
    private ElevatorService elevatorService;

    @Mock
    private Map<Integer, ElevatorManagementImpl> elevatorMap;

    @Test
    public void whenCallHasSameLevelAndTargetLevel_thenPickupThrowsRuntimeException() {
        Call call = new Call(1, 13, 13);
        Exception exception = Assert.assertThrows(InvalidLevelException.class, () -> elevatorService.pickup(call));

        String expectedMessage = "Target level should not be the same as level!";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenCallHasInvalidElevatorId_thenPickupThrowsRuntimeException() {
        Call call = new Call(2, 4, 13);
        Exception exception = Assert.assertThrows(NotExistentElevatorException.class, () -> elevatorService.pickup(call));

        String expectedMessage = "Called elevator does not exists!";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void whenUpdateElevatorHasInvalidElevatorId_thenUpdateThrowsRuntimeException() {
        UpdateElevator updateElevator = new UpdateElevator(1, 1,1);
        Exception exception = Assert.assertThrows(NotExistentElevatorException.class, () -> elevatorService.update(updateElevator));

        String expectedMessage = "Could not update not existent elevator!";
        String actualMessage = exception.getMessage();

        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void statusShouldReturnOneElement() {
        ReflectionTestUtils.setField(elevatorService, "elevatorMap", elevatorMap);
        Mockito.when(elevatorMap.values()).thenReturn(List.of(new ElevatorManagementImpl(1)));

        int statusListSize = elevatorService.status().size();
        Assert.assertEquals(1, statusListSize);
    }

    @Test
    public void statusShouldReturnElevatorWithIdEqualsOne() {
        ReflectionTestUtils.setField(elevatorService, "elevatorMap", elevatorMap);
        Mockito.when(elevatorMap.values()).thenReturn(List.of(new ElevatorManagementImpl(1)));

        int elevatorId = elevatorService.status().stream()
                .findFirst()
                .map(ElevatorData::getId)
                .orElse(-1);
        Assert.assertEquals(1, elevatorId);
    }
}
