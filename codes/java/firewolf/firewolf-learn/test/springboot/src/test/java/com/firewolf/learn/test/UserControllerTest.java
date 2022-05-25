package com.firewolf.learn.test;

import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/27
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Resource
    private MockMvc mockMvc;

    @SpyBean
    private UserService userService;

    @Test
    void findUser() throws Exception {
        doCallRealMethod().when(userService).findByName(anyString());
        MvcResult mockResult = mockMvc
                .perform(get("/user").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "liuxing")) //传入参数
                .andExpect(status().isOk()) // 断言状态
                .andReturn(); //返回结果

        User user = JSONObject.parseObject(mockResult.getResponse().getContentAsString(), User.class);
        assertEquals(user.getName(), "liuxing");
    }


    @Test
    void findUser2() throws Exception {
        doCallRealMethod().when(userService).findByName(anyString());
        mockMvc
                .perform(get("/user").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "liuxing")) //传入参数
                .andExpect(jsonPath("$.age").isNumber())
                .andExpect(jsonPath("$.name").value("liuxing"))
                .andDo(MockMvcResultHandlers.print(System.out))
                .andDo(mvcResult -> assertTrue(mvcResult.getResponse().getContentAsString().contains("liuxing")));
    }
}