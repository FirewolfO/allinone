package com.firewolf.busi.example.springtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @SpyBean
    private UserService userService;

    @Test
    void addUser() throws Exception {
        doNothing().when(userService).addUser(any(User.class));
        MvcResult mockResult = mockMvc
                .perform(post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("account","liuxing").param("password","123345"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("success",mockResult.getResponse().getContentAsString());
    }

}