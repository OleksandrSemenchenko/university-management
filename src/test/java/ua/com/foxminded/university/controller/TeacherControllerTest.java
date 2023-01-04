package ua.com.foxminded.university.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.university.model.TeacherModel;
import ua.com.foxminded.university.service.TeacherService;

class TeacherControllerTest {
    
    private TeacherService<TeacherModel> teacherServiceMock = Mockito.mock(TeacherService.class);
    private TeacherController teacherController = new TeacherController(teacherServiceMock);
    private MockMvc mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();

    @Test
    void shuldRenderTeachersListView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/index").param("getAllTeachers", "#"))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.model().attributeExists("teachers"))
               .andExpect(MockMvcResultMatchers.view().name("teachers/list"));
    }
}
