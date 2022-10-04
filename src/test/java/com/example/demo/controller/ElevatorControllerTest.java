package com.example.demo.controller;

import com.example.demo.elevator.controller.impl.ElevatorControllerImpl;
import com.example.demo.elevator.model.Call;
import com.example.demo.elevator.model.ElevatorData;
import com.example.demo.elevator.model.UpdateElevator;
import com.example.demo.elevator.service.impl.ElevatorServiceImpl;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc(addFilters = false)
public class ElevatorControllerTest {
    private MockMvc mvc;
    @MockBean
    private ElevatorServiceImpl service;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.standaloneSetup(new ElevatorControllerImpl(service)).build();
    }

    @Test
    public void shouldAddCall() throws Exception {
        Object object = new Object() {
            private final int elevatorId = 1;
            private final int level = 2;
            private final int targetLevel = 3;
        };

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String jsonObject = objectMapper.writeValueAsString(object);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/elevator")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonObject);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(service).pickup(ArgumentMatchers.any(Call.class));
    }

    @Test
    public void shouldPickupReturnBadRequestStatus() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/elevator")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateElevatorState() throws Exception {
        Object object = new Object() {
            private final int id = 1;
            private final int currentLevel = 13;
            private final int targetLevel = 4;
        };

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String json = mapper.writeValueAsString(object);

        ElevatorData elevator = ElevatorData.builder()
                .id(1)
                .currentLevel(13)
                .targetLevel(4)
                .build();

        Mockito.when(service.update(ArgumentMatchers.any(UpdateElevator.class))).thenReturn(elevator);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/elevator")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentDirection").value("STAY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentLevel").value(13))
                .andExpect(MockMvcResultMatchers.jsonPath("$.targetLevel").value(4));

        Mockito.verify(service).update(ArgumentMatchers.any(UpdateElevator.class));
    }

    @Test
    public void shouldUpdateElevatorStateReturnBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/elevator")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldPerformStep() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/elevator/step"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void shouldReturnStatus() throws Exception {
        Mockito.when(service.status()).thenReturn(List.of(ElevatorData.builder().id(1).build()));

        mvc.perform(MockMvcRequestBuilders.get("/elevator"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currentDirection").value("STAY"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].currentLevel").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].targetLevel").value(0));

        Mockito.verify(service).status();
    }
}
