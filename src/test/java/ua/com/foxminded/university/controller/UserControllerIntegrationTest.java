package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import ua.com.foxminded.university.entity.UserEntity;
import ua.com.foxminded.university.model.Authority;
import ua.com.foxminded.university.model.UserAuthorityModel;
import ua.com.foxminded.university.model.UserModel;
import ua.com.foxminded.university.repository.UserRepository;

@TestPropertySource(locations = {"/application.properties"})
@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
class UserControllerIntegrationTest {
    
    public static final String LAST_NAME = "Musk";
    public static final String FIRST_NAME = "Elon";
    public static final String USERS_EDIT_URL = "/users/edit";
    public static final String USERS_LIST_URL = "/users/list";
    public static final String EMAIL_NAME = "gmail@com";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "newpassword";
    
//    @Container
//    public static PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:latest")
//            .withDatabaseName("university");
    
/*    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory; 
    */
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserController userController;
    
    @Autowired
    private UserDetailsManager userDetailsManager;
    
    @Autowired
    private MockMvc mockMvc;
    
    private UserEntity userEntity;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        
        userEntity = new UserEntity();
        userEntity.setEmail(EMAIL_NAME);
        userEntity.setEnabled(true);
        userEntity.setFirstName(EMAIL_NAME);
        userEntity.setLastName(LAST_NAME);
        
//        userRepository.saveAndFlush(userEntity);
        
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
//        entityManager.persist(userEntity);
//        entityManager.getTransaction().commit();
        
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails user = User.builder().username(EMAIL_NAME)
                               .password(PASSWORD)
                               .passwordEncoder(encoder::encode)
                               .authorities(Authority.ADMIN.toString())
                               .disabled(false)
                               .build();
        userDetailsManager.createUser(user);
    }
    /*
    @AfterEach
    void cleanUp() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        UserEntity persistedUser = entityManager.find(UserEntity.class, userEntity.getId());
        entityManager.remove(persistedUser);
    }
    */
    @Test
    void edit_shouldEditUserDetails() throws Exception {
        UserModel userModel = new UserModel();
        userModel.setEnabled(false);
        userModel.setPassword(NEW_PASSWORD);
        userModel.setUserAuthority(new UserAuthorityModel());
        userModel.getUserAuthority().setAuthority(Authority.STAFF);
       
        
        mockMvc.perform(MockMvcRequestBuilders.post(USERS_EDIT_URL)
                                              .flashAttr("userModel", userModel)
                                              .param("email", EMAIL_NAME))
               .andDo(print())
               .andExpect(redirectedUrl(USERS_LIST_URL));
    }
    
/*
    @Test
    void authorize_shouldAuthorizeExistingUser() throws Exception {
            UserModel userModel = new UserModel();
            userModel.setEmail("@@@@@@@@");
            
            mockMvc.perform(MockMvcRequestBuilders.post("/users/authorize")
                                                  .flashAttr("userModel",userModel)
                                                  .param("password", "4")
                                                  .param("passwordConfirm", "4"))
                   .andDo(print())
                   .andExpect(redirectedUrl("/users/list"));
    }
    
    @Test
    void listAllUsers_shouldRenderUserList() throws Exception {
        mockMvc.perform(get("/users/list")).andExpect(status().isOk())
                                           .andExpect(model().attributeExists("users"))
                                           .andExpect(model().attributeExists("userModel"));
    }
    */
}
