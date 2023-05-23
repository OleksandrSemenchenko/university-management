package ua.com.foxminded.university.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import ua.com.foxminded.university.entity.Course;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entitymother.CourseMother;
import ua.com.foxminded.university.entitymother.GroupMother;
import ua.com.foxminded.university.entitymother.TimingMother;
import ua.com.foxminded.university.entitymother.LessonMother;
import ua.com.foxminded.university.entitymother.TimetableMother;

@DataJpaTest
@ActiveProfiles("test")
class LessonRepositoryTest {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    private Course course;
    private Group group;
    private Lesson lesson;
    private Timetable timetable;
    private Timing lessonTiming;
    
    @BeforeEach
    void init() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        
        timetable = TimetableMother.complete().build();
        entityManager.persist(timetable);
        
        lessonTiming = TimingMother.complete().timetable(timetable).build();
        entityManager.persist(lessonTiming);
        
        course = CourseMother.complete().build();
        entityManager.persist(course);
        
        group = GroupMother.complete().build();
        entityManager.persist(group);
        
        lesson = LessonMother.complete().course(course)
                                              .group(group).build();
        entityManager.persist(lesson);
        
        entityManager.getTransaction().commit();
        entityManager.close();
    }
    
    void findByDatestampAndGroupIdAndLessonTimingId_ShouldReturnScheduleEntity() {
        Lesson entity = lessonRepository.findByDatestampAndGroupIdAndTimingId(
                lesson.getDatestamp(), group.getId(), lessonTiming.getId());
        assertEquals(lesson.getGroup().getId(), entity.getGroup().getId());
    }
    
    @Test
    void findByDatestamp_ShouldReturnDayLessonsWithTimetableRelationship() {
        List<Lesson> lessons = lessonRepository.findByDatestamp(
                lesson.getDatestamp());
        assertEquals(lesson.getDatestamp(), lessons.iterator().next().getDatestamp());
    }
    
    @Test
    void findCourseById_ShouldReturnCourseOwnedByTimetableWithId() {
        Lesson receivedSchedule = lessonRepository.findCourseById(lesson.getId());
        assertEquals(course.getId(), receivedSchedule.getCourse().getId());
    }
        
    @Test
    void findGroupById_ShouldReturnGroupOwnedByTimetableWithId() {
        Lesson receivedSchedule = lessonRepository.findGroupById(
                lesson.getId());
        assertEquals(group.getId(), receivedSchedule.getGroup().getId());
    }
    
    @Test
    void findById_ShouldReturnTimetableEntityWithId() {
        Lesson receivedSchedule = lessonRepository.findById(
                lesson.getId().intValue());
        assertEquals(lesson.getId(), receivedSchedule.getId());
    }
}
