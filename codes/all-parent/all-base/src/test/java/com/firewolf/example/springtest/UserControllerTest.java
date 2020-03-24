package com.firewolf.example.springtest;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private UserService userService;

    @Test
    void addUser() throws Exception {
        doNothing().when(userService).addUser(any(User.class));
        MvcResult mockResult = mockMvc
                .perform(post("/users").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "liuxing").param("password", "123345"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals("success", mockResult.getResponse().getContentAsString());
    }

    @Test
    void testSelectUser() throws Exception {
        List<User> users = Arrays.asList(
                User.builder().account("lx").build(),
                User.builder().account("liuxing").build()
        );

        doReturn(users).when(userService).selectUser();

        //使用JsonPath解析结果并进行断言
        mockMvc.perform(get("/users"))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$..account").isArray())
                .andExpect(jsonPath("$.[0].account").value("lx"))
                .andDo(MockMvcResultHandlers.print(System.out))
                .andDo(mvcResult -> assertTrue(mvcResult.getResponse().getContentAsString().contains("liuxing")));
    }

}