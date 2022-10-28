package ua.com.foxminded.university.dao.jdbc;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.university.config.AppConfigTest;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.TeacherEntity;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TeacherRepository;
import ua.com.foxminded.university.repository.jdbc.TeacherJdbcRepository;

@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ContextConfiguration(classes = AppConfigTest.class)
@ExtendWith(SpringExtension.class)
class TeacherJdbcRepositoryTest {
    
    private static final String COURSE_DESCRIPTION = "some description";
    private static final String COURSE_NAME = "Programming";
    private static final String TEACHER_LAST_NAME = "Ritchie";
    private static final String NEW_TEACHER_LAST_NAME = "Musk";
    private static final String NEW_TEACHER_FIRST_NAME = "Elon";
    private static final String TEACHER_FIRST_NAME = "Dennis";
    private static final int NEW_TEACHER_ID = 2;
    private static final int TEACHER_ID = 1;
    private static final int COURSE_ID_NUMBER = 2;
    private static final int FIST_ELEMENT = 0;
    private static final int COURSES_QUANTITY = 2;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @BeforeEach
    void init() {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(TEACHER_FIRST_NAME);
        teacher.setLastName(TEACHER_LAST_NAME);
        entityManager.persist(teacher);
        
        CourseEntity course = new CourseEntity();
        course.setDescription(COURSE_DESCRIPTION);
        course.setName(COURSE_NAME);
        course.setTeacher(teacher);
        entityManager.persist(course);
    }
    
    @Test
    void getCourseListByTeacherId_ReceivingTeacherDatabaseData_CorrectReceivedData() 
            throws RepositoryException {
        TeacherRepository teacherDao = new TeacherJdbcRepository(entityManager);
        TeacherEntity teacherData = teacherDao.getCourseListByTeacherId(TEACHER_ID);
        
        assertEquals(TEACHER_ID, teacherData.getId());
        assertEquals(TEACHER_FIRST_NAME, teacherData.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacherData.getLastName());
        assertEquals(TEACHER_ID, teacherData.getCourseList().get(FIST_ELEMENT).getTeacher().getId());
        assertEquals(COURSE_ID_NUMBER, teacherData.getCourseList().get(FIST_ELEMENT).getId());
        assertEquals(COURSE_NAME, teacherData.getCourseList().get(FIST_ELEMENT).getName());
        assertEquals(COURSE_DESCRIPTION, teacherData.getCourseList().get(FIST_ELEMENT).getDescription());
        assertEquals(COURSES_QUANTITY, teacherData.getCourseList().size());
    }
    
    @Test
    void getById_ReceivingTeacherDatabaseData_CorrectReceivedData() throws RepositoryException {
        TeacherRepository teacherDao = new TeacherJdbcRepository(entityManager);
        TeacherEntity teacher = teacherDao.getById(TEACHER_ID);
        
        assertEquals(TEACHER_FIRST_NAME, teacher.getFirstName());
        assertEquals(TEACHER_LAST_NAME, teacher.getLastName());
        assertEquals(TEACHER_ID, teacher.getId());
    }
    
    @Test
    void deleteById_DeletingTeacherDatabaseData_DatabaseHasNoData() throws RepositoryException {
        TeacherRepository teacherDao = new TeacherJdbcRepository(entityManager);
        teacherDao.deleteById(TEACHER_ID);
        TeacherEntity teacher = new TeacherEntity();
        teacher.setId(TEACHER_ID);
        
        boolean persisentTeacher = entityManager.contains(teacher);
        assertFalse(persisentTeacher);
    }
    
    
    @Test
    void insert_InsertingTeacherDatabaseData_DatabaseHasCorrectData() throws RepositoryException {
        TeacherEntity teacher = new TeacherEntity();
        teacher.setFirstName(NEW_TEACHER_FIRST_NAME);
        teacher.setLastName(NEW_TEACHER_LAST_NAME);
        TeacherRepository teacherDao = new TeacherJdbcRepository(entityManager);
        TeacherEntity teacherWithId = teacherDao.insert(teacher);

        TeacherEntity insertedTeacher = entityManager.find(TeacherEntity.class, NEW_TEACHER_ID);
        
        assertEquals(NEW_TEACHER_ID, teacherWithId.getId());
        assertEquals(NEW_TEACHER_FIRST_NAME, insertedTeacher.getFirstName());
        assertEquals(NEW_TEACHER_LAST_NAME, insertedTeacher.getLastName());
    }
    
    @Test
    void update_UpdatingTeacherDatabaseData_DatabaseHasCorrectData() throws RepositoryException {
        TeacherEntity teacherData = new TeacherEntity();
        teacherData.setId(TEACHER_ID);
        teacherData.setFirstName(NEW_TEACHER_FIRST_NAME);
        teacherData.setLastName(NEW_TEACHER_LAST_NAME);
        TeacherRepository teacherDao = new TeacherJdbcRepository(entityManager);
        teacherDao.update(teacherData);
        
        TeacherEntity updatedTeacherData = entityManager.find(TeacherEntity.class, TEACHER_ID);
        
        assertEquals(TEACHER_ID, updatedTeacherData.getId());
        assertEquals(NEW_TEACHER_FIRST_NAME, updatedTeacherData.getFirstName());
        assertEquals(NEW_TEACHER_LAST_NAME, updatedTeacherData.getLastName());
    }
    
}
