package com.webproject.pms.controller.user;

import com.webproject.pms.model.dao.LetterDao;
import com.webproject.pms.model.entities.Letter;
import com.webproject.pms.model.entities.User;
import com.webproject.pms.service.impl.LetterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class LetterControllerTest {

    private Letter letter;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    LetterController letterController;
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private Principal principal;
    @MockBean
    private LetterServiceImpl letterService;

    @BeforeEach
    void setUp() {

        User user = new User();
        user.setUserId(1L);

        letter = new Letter();
        letter.setLetterId(1L);
        letter.setUser(user);
        letter.setDescription("some text");

        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testController() {
        assertThat(letterController).isNotNull();
    }

    @Test
    @WithMockUser("spring")
    void supportForm() throws Exception {

        mockMvc.perform(get("/support")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userSupport"));

    }

    @Test
    @WithMockUser("spring")
    void createSupport() throws Exception {

        doReturn(true).when(letterService).addNewLetter(letter, principal);

        mockMvc.perform(post("/support")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userSupport"));
    }
}