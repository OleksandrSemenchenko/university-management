package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.dto.LessonDTO;
import ua.com.foxminded.university.entity.Timetable;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.entity.Lesson;
import ua.com.foxminded.university.entity.Timing;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.repository.GroupRepository;
import ua.com.foxminded.university.repository.LessonRepository;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.repository.TimingRepository;
import ua.com.foxminded.university.service.LessonService;

@Service
@Transactional
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    
    public static final int ONE_WEEK = 1;
    public static final int NO_LESSONS = 0;
    public static final int DEFFALUT_LESSONS_QUANTITY = 5;
    public static final int OFFSET = 1;
    public static final int FIRST_ELEMENT = 0;
    public static final int WEEKS_OFFSET = 3;
    public static final int WEEKS_QUANTITY = 4;
    public static final int END_WEEK_DAY_NUMBER = 7;
    public static final int START_WEEK_DAY_NUMBER = 0;
    public static final int ONE_DAY = 1;
    public static final Type LESSON_MODELS_LIST_TYPE = 
            new TypeToken<List<LessonDTO>>() {}.getType();
    
    private final ModelMapper modelMapper;
    private final LessonRepository lessonRepository;
    private final TimingRepository timingRepository;
    private final TimetableRepository timetableRepository;
    private final GroupRepository groupRepository;
    
    @Override
    public LocalDate moveWeekBack(LocalDate date) {
        return date.plusWeeks(ONE_WEEK);
    }

    @Override
    public LocalDate moveWeekForward(LocalDate date) {
        return date.minusWeeks(ONE_WEEK);
    }
    
    @Override
    public List<List<LessonDTO>> getWeekLessonsOwnedByTeacher(LocalDate date, int teacherId) 
            throws ServiceException {
        
        LocalDate monday = findMondayOfWeek(date);
        int lessonsQuantity = defineMaxNumberOfDayLessonsInWeekForTeacher(date, teacherId);
        
        if (lessonsQuantity < DEFFALUT_LESSONS_QUANTITY) {
            lessonsQuantity = DEFFALUT_LESSONS_QUANTITY;
        }
        
        List<List<LessonDTO>> lessons = new ArrayList<>();
       
        try {
            for (int i = 0; i < lessonsQuantity; i++) {
                List<LessonDTO> lessonsOfWeekContainingIdemOrder = new ArrayList<>();
                
                for (int j = 0; j < DayOfWeek.values().length; j++) {
                    LocalDate datestamp = monday.plusDays(j);
                    Lesson lesson = lessonRepository
                            .findByDatestampAndTeacherIdAndLessonOrder(datestamp, teacherId, i);
                    if (lesson == null) {
                        lessonsOfWeekContainingIdemOrder.add(LessonDTO.builder()
                                .datestamp(datestamp)
                                .build());
                    } else {
                        LessonDTO lessonDto = modelMapper.map(lesson, LessonDTO.class);
                        lessonsOfWeekContainingIdemOrder.add(lessonDto);  
                    }
                }
                addLessonTiming(lessonsOfWeekContainingIdemOrder);
                lessons.add(lessonsOfWeekContainingIdemOrder);
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Getting lessons owned by the teacher fails", e); 
        }
        
        return lessons;
    }
    
    @Override
    public List<LessonDTO> applyTimetable(LocalDate date, int timetableId) 
            throws ServiceException {
        Timetable timetable = timetableRepository.findById(timetableId);
        List<Lesson> lessons = lessonRepository.findByDatestamp(date);
        lessons.stream().forEach(lesson -> lesson.setTimetable(timetable));
        try {
            lessonRepository.saveAllAndFlush(lessons);
            return modelMapper.map(lessons, LESSON_MODELS_LIST_TYPE);
        } catch (DataAccessException e) {
            throw new ServiceException("Applying timetable for lessons fails", e);
        }
    }

    @Override
    public void sortByLessonOrder(List<LessonDTO> lessons) {
        Collections.sort(lessons, Comparator.comparing(LessonDTO::getLessonOrder));
    }

    @Override
    public void addLessonTiming(List<LessonDTO> lessons) {
        lessons.stream().forEach(this::addLessonTiming);
    }

    @Override
    public void addLessonTiming(LessonDTO lesson) {
        if (lesson.hasTimetable()) {
            List<Timing> timings = timingRepository.findByTimetableId(
                    lesson.getTimetable().getId());
            Optional<Timing> timing = timings.stream()
                    .sorted(Comparator.comparing(Timing::getStartTime))
                    .skip(lesson.getLessonOrder() - OFFSET).findFirst();

            if (timing.isPresent()) {
                lesson.setStartTime(timing.get().getStartTime());
                lesson.setEndTime(timing.get().getStartTime().plus(timing.get().getLessonDuration()));
            }
        }
    }

    @Override
    public LocalDate moveMonthForward(LocalDate date) {
        return date.plusWeeks(WEEKS_OFFSET);
    }

    @Override
    public LocalDate moveMonthBack(LocalDate date) {
        return date.minusWeeks(WEEKS_OFFSET);
    }

    @Override
    public void deleteById(Integer id) throws ServiceException {
        try {
            lessonRepository.deleteById(id);
        } catch (DataAccessException | IllegalArgumentException e) {
            throw new ServiceException("Deleting timetable with id = " + id + "", e);
        }
    }

    @Override
    public void update(LessonDTO model) throws ServiceException {
        try {
            Lesson entity = modelMapper.map(model, Lesson.class);
            Lesson persistEntity = lessonRepository.findById(model.getId().intValue());
            persistEntity.setCourse(entity.getCourse());
            persistEntity.setDatestamp(entity.getDatestamp());
            persistEntity.setDescription(entity.getDescription());
            persistEntity.setLessonOrder(entity.getLessonOrder());
            persistEntity.setTeacher(entity.getTeacher());
            persistEntity.setTimetable(entity.getTimetable());
            persistEntity.setGroups(entity.getGroups());
            lessonRepository.saveAndFlush(persistEntity);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Updating timetable failes", e);
        }
    }

    @Override
    public LessonDTO create(LessonDTO lessonDto) throws ServiceException {
        try {
            Lesson persistedLesson = lessonRepository.findByDatestampAndLessonOrderAndGroupsId(
                    lessonDto.getDatestamp(),
                    lessonDto.getLessonOrder(), 
                    lessonDto.getGroups().iterator().next().getId());
            
            Lesson counterpartLesson = lessonRepository
                    .findByDatestampAndTeacherIdAndLessonOrderAndCourseId(
                            lessonDto.getDatestamp(),
                            lessonDto.getTeacher().getId(),
                            lessonDto.getLessonOrder(),
                            lessonDto.getCourse().getId());
            
            Lesson lesson = modelMapper.map(lessonDto, Lesson.class);
            
            if (persistedLesson == null && counterpartLesson == null) {
                
                Lesson createdEntity = lessonRepository.saveAndFlush(lesson);
                return modelMapper.map(createdEntity, LessonDTO.class);
            } else if (persistedLesson == null) {
                int groupId = lesson.getGroups().iterator().next().getId();
                Group group = groupRepository.findById(groupId); 
                counterpartLesson.addGroup(group);
                Lesson updatedLesson = lessonRepository.saveAndFlush(counterpartLesson);
                return modelMapper.map(updatedLesson, LessonDTO.class);
            } else {
                return modelMapper.map(persistedLesson, LessonDTO.class);
            }
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Creating a timetable fails", e);
        }
    }

    @Override
    public LessonDTO getById(int id) throws ServiceException {
        try {
            Lesson entity = lessonRepository.findById(id);
            return modelMapper.map(entity, LessonDTO.class);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetable by ID fails", e);
        }
    }

    @Override
    public List<List<List<LessonDTO>>> getMonthLessons(LocalDate date) 
            throws ServiceException {

        List<List<List<LessonDTO>>> monthTimetable = new ArrayList<>();

        for (int i = 0; i < WEEKS_QUANTITY; i++) {
            LocalDate datestamp = date.plusWeeks(i);
            List<List<LessonDTO>> weekTimetables = getWeekLessons(datestamp);
            monthTimetable.add(weekTimetables);
        }
        return monthTimetable;
    }

    @Override
    public List<LessonDTO> getDayLessons(LocalDate date) throws ServiceException {
        try {
            List<Lesson> entities = lessonRepository.findByDatestamp(date);
            List<LessonDTO> models = modelMapper.map(entities, LESSON_MODELS_LIST_TYPE);

            if (models.isEmpty()) {
                models = new ArrayList<>();
                LessonDTO model = new LessonDTO();
                model.setDatestamp(date);
                models.add(model);
            }

            return models;
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting timetables of day fails", e);
        }
    }

    @Override
    public List<LessonDTO> getAll() throws ServiceException {
        try {
            List<Lesson> timetableEntities = lessonRepository.findAll();
            return modelMapper.map(timetableEntities, LESSON_MODELS_LIST_TYPE);
        } catch (DataAccessException | IllegalArgumentException | 
                 ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    private int defineMaxNumberOfDayLessonsInWeekForTeacher(LocalDate datestamp, 
            int teacherId) throws ServiceException {
        
        List<List<Lesson>> weekLessons = new ArrayList<>();
        
        try {
            for (int i = 0; i < DayOfWeek.values().length; i++) {
                List<Lesson> lessons = lessonRepository.findByDatestampAndTeacherId(
                        datestamp, teacherId);
                weekLessons.add(lessons);
            }
        } catch (DataAccessException e) {
            throw new ServiceException("Find lessons by date and teacher id failed", e);
        }
        
        Optional<Integer> lessonsNumber = weekLessons.stream()
                .map(lessons -> lessons.size()).max(Integer::compareTo);
        
        if (lessonsNumber.isPresent()) {
            return lessonsNumber.get();
        } else {
            return NO_LESSONS;
        }
    }
    
    private List<List<LessonDTO>> getWeekLessons(LocalDate date) throws ServiceException {

        LocalDate startDayOfWeek = findMondayOfWeek(date);
        List<List<LessonDTO>> weekTimetables = new ArrayList<>();
        List<LessonDTO> dayTimetables;

        for (int i = 0; i < DayOfWeek.values().length; i++) {
            dayTimetables = getDayLessons(startDayOfWeek.plusDays(i));
            weekTimetables.add(dayTimetables);
        }
        return weekTimetables;
    }

    private LocalDate findMondayOfWeek(LocalDate date) {
        LocalDate startDayOfWeek = date;

        if (startDayOfWeek.getDayOfWeek() == DayOfWeek.MONDAY) {
            return startDayOfWeek;
        } else {
            while (startDayOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
                startDayOfWeek = startDayOfWeek.minusDays(ONE_DAY);
            }
            return startDayOfWeek;
        }
    }
}
