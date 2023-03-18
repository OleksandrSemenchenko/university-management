package ua.com.foxminded.university.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.GroupModel;
import ua.com.foxminded.university.model.StudentModel;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.StudentRepository;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"prod", "testcontainers"})
class StudentControllerIntegrationTest {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private TimetableRepository timetableRepository;
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private StudentService<StudentModel> studentService;
    
    @Autowired
    private GroupService<GroupModel> groupService;
    
    @Autowired
    private MockMvc mockMvc;
    
    
    @BeforeEach
    void setup() {
//        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService, 
//                                                                        groupService))
//                                 .build();
        studentRepository.deleteAll();
        groupRepository.deleteAll();
        timetableRepository.deleteAll();
        
    }
    
    @Test
    @WithUserDetails("a")
    void getAllStudents_shouldRenderStudentsList() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("students"))
               .andExpect(view().name("students/list"));
    }
}
