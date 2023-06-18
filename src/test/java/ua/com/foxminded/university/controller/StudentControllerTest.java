package ua.com.foxminded.university.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ua.com.foxminded.university.controller.DefaultController.*;
import static ua.com.foxminded.university.controller.DefaultControllerTest.ERROR_VIEW;
import static ua.com.foxminded.university.controller.GroupController.GROUPS_ATTRIBUTE;
import static ua.com.foxminded.university.controller.StudentController.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.dto.StudentDTO;
import ua.com.foxminded.university.dtomother.StudentDTOMother;
import ua.com.foxminded.university.service.GroupService;
import ua.com.foxminded.university.service.StudentService;

@ExtendWith(SpringExtension.class)
class StudentControllerTest {
    
    public static final int STUDENT_ID = 1;
    
    @MockBean
    private StudentService studentServiceMock;
    
    @MockBean
    private GroupService groupServiceMock;
    
    private MockMvc mockMvc;
    private StudentDTO studentDto;
    
    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(
                studentServiceMock, groupServiceMock)).build();
        studentDto = StudentDTOMother.complete().build();
    }
    
    @Test
    void create_ShouldReturnBadRequestStatus() throws Exception {
        studentDto.setUser(null);
        mockMvc.perform(post("/students/create").flashAttr("studentModel", studentDto))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(view().name(ERROR_VIEW));
    }
    
    @Test
    void create_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/students/create")
                    .flashAttr(STUDENT_ATTRIBUTE, studentDto))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(STUDENTS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(studentServiceMock).create(isA(StudentDTO.class));
    }
    
    @Test
    void getAll_ShouldCallServicesAndRenderListView() throws Exception {
        mockMvc.perform(get("/students/list"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(model().attributeExists(STUDENT_ATTRIBUTE, 
                                                  STUDENTS_ATTRIBUTE, 
                                                  GROUPS_ATTRIBUTE))
               .andExpect(view().name(STUDENTS_LIST_TEMPLATE_PATH));
        
        verify(studentServiceMock).getAll();
        verify(groupServiceMock).getAll();
    }
    
    @Test
    void update_ShouldReturnBadRequestStatus() throws Exception {
        studentDto.setUser(null);
        mockMvc.perform(post("/students/{studentId}/update", STUDENT_ID)
                    .flashAttr("studentModel", studentDto))
               .andDo(print())
               .andExpect(status().is4xxClientError())
               .andExpect(view().name(ERROR_VIEW));
    }
    
    @Test
    void update_ShouldRedirectToGetAll() throws Exception {
        mockMvc.perform(post("/students/{studentId}/update", STUDENT_ID)
                    .flashAttr(STUDENT_ATTRIBUTE, studentDto))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(STUDENTS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(studentServiceMock).update(isA(StudentDTO.class));
    }
    
    @Test
    void delete_ShouldCallStudentServiceAndRedirect() throws Exception {
        mockMvc.perform(post("/students/delete").param("studentId", String.valueOf(STUDENT_ID)))
               .andDo(print())
               .andExpect(redirectedUrl(new StringBuilder().append(SLASH)
                                                           .append(STUDENTS_LIST_TEMPLATE_PATH)
                                                           .toString()));
        verify(studentServiceMock).deleteById(anyInt());
    }
}
