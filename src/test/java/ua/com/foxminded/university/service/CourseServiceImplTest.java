package ua.com.foxminded.university.service;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ua.com.foxminded.university.service.impl.CourseServiceImpl.COURSE_MODEL_LIST_TYPE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import ua.com.foxminded.university.dto.CourseDTO;
import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Teacher;
import ua.com.foxminded.university.entitymother.CourseMother;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.modelmother.CourseDtoMother;
import ua.com.foxminded.university.repository.CourseRepository;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.service.impl.CourseServiceImpl;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    
    private static final int ID = 1;
    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID = 1;
    
    @InjectMocks
    private CourseServiceImpl courseService;
    
    @Mock
    private TeacherRepository teacherRepositoryMock;
    
    @Mock
    private CourseRepository courseRepositoryMock;
    
    @Mock
    private ModelMapper modelMapperMock;
    
    private Course course;
    private CourseDTO courseDto;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        courseDto = CourseDtoMother.complete().build();
        course = CourseMother.complete().teachers(new HashSet<>()).build();
        teacher = Teacher.builder().courses(new HashSet<>()).build();
    }
    
    @Test
    void deassignTeacherToCourse_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(courseRepositoryMock.findById(anyInt())).thenReturn(course);
        when(teacherRepositoryMock.findById(anyInt())).thenReturn(teacher);
        courseService.deassignTeacherToCourse(TEACHER_ID, COURSE_ID);
        verify(courseRepositoryMock).saveAndFlush(isA(Course.class));
    }
    
    @Test
    void assignTeacherToCourse_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(courseRepositoryMock.findById(anyInt())).thenReturn(course);
        when(teacherRepositoryMock.findById(anyInt())).thenReturn(teacher);
        courseService.assignTeacherToCourse(TEACHER_ID, COURSE_ID);
        verify(courseRepositoryMock).saveAndFlush(isA(Course.class));
    }
    
    @Test
    void getTimetableAndTeachersByCourseId_ShouldExecuteCorrecCallsQuantity() 
            throws ServiceException {
        when(courseRepositoryMock.getCourseRelationsById(anyInt()))
            .thenReturn(course);
        courseService.getByIdWithLessonsAndTeachers(ID);
        verify(modelMapperMock).map(course, CourseDTO.class);
    }
    
    void update_ShouldExcecuteCorrectCallsQuantity() throws ServiceException {
        courseService.update(courseDto);
        InOrder inOrder = Mockito.inOrder(modelMapperMock, courseRepositoryMock);
        inOrder.verify(modelMapperMock).map(courseDto, CourseDTO.class);
        inOrder.verify(courseRepositoryMock).saveAndFlush(course);
    }
    
    @Test
    void getAll_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        Course course = CourseMother.complete().build();
        List<Course> courses = Arrays.asList(course);
        when(courseRepositoryMock.findAll()).thenReturn(courses);
        courseService.getAll();
        verify(modelMapperMock).map(courses, COURSE_MODEL_LIST_TYPE);
    }
    
    @Test
    void create_ShouldExcecuteCorrectCallsQuantity() throws ServiceException {
        when(modelMapperMock.map(courseDto, Course.class)).thenReturn(course);
        courseService.create(courseDto);
        verify(courseRepositoryMock).saveAndFlush(ArgumentMatchers.isA(Course.class));
    }
    
    @Test
    void deleteById_ShouldExcecuteCorrectCallsQauntity() throws ServiceException {
        courseService.deleteById(ID);
        verify(courseRepositoryMock).deleteById(anyInt());
    }
    
    @Test
    void getById_ShouldExecuteCorrectCallsQuantity() throws ServiceException {
        when(courseRepositoryMock.findById(anyInt())).thenReturn(course);
        courseService.getById(ID);
        verify(modelMapperMock).map(course, CourseDTO.class);
    }
}
