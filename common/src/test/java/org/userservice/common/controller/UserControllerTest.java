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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        mockMvc = createMockMvc(applicationContext);
        testUserDto = createTestUserDto();
        createdUserDto = createCreatedUserDto();
        updateUserDto = createUpdateUserDto();
        updatedUserDto = createUpdatedUserDto();
        updatedAllUserDto = createUpdatedAllUserDto();
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
    void updateAllFieldsUser_ReturnsUpdatedUser() throws Exception {
        when(userService.updateAllUser(any(), any())).thenReturn(updatedUserDto);
        String jsonRequest = objectMapper.writeValueAsString(updatedAllUserDto);
        MvcResult result = mockMvc.perform(put("/v1/users/{userId}", USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), UserDto.class);
        assertEquals(updatedUserDto, actual);
    }

    @Test
    void updateSomeFieldsUser_ReturnsUpdatedUser() throws Exception {
        when(userService.updateUser(any(), any())).thenReturn(updatedUserDto);
        String jsonRequest = objectMapper.writeValueAsString(updateUserDto);
        MvcResult result = mockMvc.perform(patch("/v1/users/{userId}", USER_ID)
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

    private static CreateRequestUserDto createUpdatedAllUserDto() {
        return new CreateRequestUserDto(
                "newEmail@example.com",
                "Niki",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "1234567890"
        );
    }

    private static UserDto createUpdatedUserDto() {
        return UserDto.builder()
                .id(USER_ID)
                .email("newEmail@example.com")
                .firstName("Niki")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("1234567890")
                .build();
    }

    private static UserDto createUpdateUserDto() {
        return UserDto.builder()
                .email("newEmail@example.com")
                .firstName("Niki")
                .build();
    }

    private static UserDto createCreatedUserDto() {
        return UserDto.builder()
                .id(USER_ID)
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .address("123 Main St")
                .phoneNumber("1234567890")
                .build();
    }

    private static CreateRequestUserDto createTestUserDto() {
        return new CreateRequestUserDto(
                "test@example.com",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "1234567890"
        );
    }

    private static MockMvc createMockMvc(WebApplicationContext applicationContext) {
        return MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }
}
