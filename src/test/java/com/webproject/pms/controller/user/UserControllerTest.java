package com.webproject.pms.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webproject.pms.model.dao.UserDao;
import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerTest {

    private User user;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext context;

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserDao userDao;
    @MockBean
    private UserServiceImpl userService;

    @BeforeEach
    public void setup() {

        user = new User();
        user.setUserId(1L);
        user.setPassword("123465");
        user.setRole(new Role(1L, "ROLE_USER"));

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void contextLoads() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(value = "admin", roles = "{ROLE_ADMIN}")
    void accountPage() throws Exception{

        this.mockMvc.perform(get("/my-account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/admin"));
    }

    @Test
    @Transactional
    @WithMockUser("spring")
    void showUserInfo() throws Exception {

        mockMvc.perform(get("/profile-info/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userUpdatePersonalData"));
    }

    @Test
    @Transactional
    @WithMockUser("spring")
    void updateUser() throws Exception{

        this.mockMvc.perform(post("/profile-info/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "Name")
                .param("surname", "Surname")
                .param("phone", "Phone")
                .param("email", "Email")
                .param("password", "Password")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userUpdatePersonalData"));
    }

    @Test
    @WithMockUser("spring")
    void updatePasswordForm() throws Exception {

        mockMvc.perform(get("/update-password/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userUpdatePassword"));
    }

    @Test
    @WithMockUser("spring")
    void updatePassword() throws Exception {

        this.mockMvc.perform(post("/update-password/" + user.getUserId())
                .contentType(MediaType.APPLICATION_JSON)
                .param("newPassword", "newPassword")
                .param("oldPassword", "oldPassword")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userUpdatePassword"));
    }
}