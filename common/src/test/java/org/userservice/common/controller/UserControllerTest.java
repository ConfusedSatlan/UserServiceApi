package org.userservice.common.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.userservice.common.model.dto.CreateRequestUserDto;
import org.userservice.common.model.dto.UserDto;
import org.userservice.common.service.UserService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    private static CreateRequestUserDto updatedAllUserDto;
    private static CreateRequestUserDto testUserDto;
    private static UserDto createdUserDto;
    private static UserDto updateUserDto;
    private static UserDto updatedUserDto;
    private static final Long USER_ID = 1L;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    UserController userController;

    @MockBean
    UserService userService;

    private static MockMvc mockMvc;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext applicationContext) {

        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();

        testUserDto = new CreateRequestUserDto(
                "test@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "1234567890"
        );

        createdUserDto = UserDto.builder()
                .id(USER_ID)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("1234567890")
                .build();

        updateUserDto = UserDto.builder()
                .email("newEmail@example.com")
                .firstName("Niki")
                .build();

        updatedUserDto = UserDto.builder()
                .id(USER_ID)
                .email("newEmail@example.com")
                .firstName("Niki")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("1234567890")
                .build();

        updatedAllUserDto = new CreateRequestUserDto(
                "newEmail@example.com",
                "Niki",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "1234567890"
        );
    }

    @Test
    void createUser_ValidUser_ReturnsCreated() throws Exception {
        when(userService.createUser(testUserDto)).thenReturn(createdUserDto);
        String jsonRequest = objectMapper.writeValueAsString(testUserDto);
        MvcResult result = mockMvc.perform(post("/v1/users")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        UserDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(testUserDto.email(), actual.getEmail());
    }

    @Test
    void getUserByID_ReturnsUser() throws Exception {
        when(userService.getUserById(USER_ID)).thenReturn(createdUserDto);
        MvcResult result = mockMvc.perform(get("/v1/users/{userId}", USER_ID))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertNotNull(actual);
        assertNotNull(actual.getId());
        assertEquals(actual.getId(), USER_ID);
    }

    @Test
    void updateUser_ReturnsUpdatedUser() throws Exception {
        when(userService.updateUser(any(), any())).thenReturn(updatedUserDto);
        String jsonRequest = objectMapper.writeValueAsString(updateUserDto);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/users/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(updatedUserDto, actual);
    }

    @Test
    void deleteUser_ReturnsSuccessMessage() throws Exception {
        when(userService.deleteUser(any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/users/{userId}", USER_ID))
                .andExpect(status().isOk());
    }
}
