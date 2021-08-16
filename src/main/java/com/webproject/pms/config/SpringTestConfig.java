package com.webproject.pms.config;

import com.webproject.pms.model.entities.Role;
import com.webproject.pms.model.entities.User;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Arrays;

@TestConfiguration
public class SpringTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        User userCustomer = new User();
        userCustomer.setUserId(1L);
        userCustomer.setRole(new Role(1L, "Role_USER"));
        userCustomer.setName("Name");
        userCustomer.setSurname("Surname");
        userCustomer.setEmail("customer@mail.com");
        userCustomer.setUsername("username");
        userCustomer.setPassword("123456");

        User adminUser = new User();
        adminUser.setUserId(2L);
        adminUser.setRole(new Role(2L, "Role_ADMIN"));
        adminUser.setName("Admin");
        adminUser.setSurname("Admin");
        adminUser.setEmail("admin@mail.com");
        adminUser.setUsername("admin");
        adminUser.setPassword("123456");

        return new InMemoryUserDetailsManager(Arrays.asList(
                userCustomer, adminUser));
    }
}
