package in.techbeat.palapa.controller;

import in.techbeat.palapa.model.request.CreateUserRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment environment;

    private Jackson2ObjectMapperBuilder objectMapperBuilder = new Jackson2ObjectMapperBuilder();

    @Test
    public void testPing() throws Exception {
        mockMvc.perform(get("/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("OK")));
    }

    @Test
    public void testUserRegistrationAndLogin() throws Exception {
        org.neo4j.ogm.config.Configuration config;
        final CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .email("test@test.com")
                .password("testPass1@")
                .username("testUser")
                .confirmPassword("testPass1@").build();
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapperBuilder.build().writeValueAsString(createUserRequest))).andExpect(status().isOk());
    }
}