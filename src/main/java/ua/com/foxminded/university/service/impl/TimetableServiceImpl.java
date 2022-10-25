package ua.com.foxminded.university.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import ua.com.foxminded.university.entity.CourseEntity;
import ua.com.foxminded.university.entity.GroupEntity;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.entity.DayOfWeek;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.repository.RepositoryException;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.ServiceException;
import ua.com.foxminded.university.service.TimetableService;

@Slf4j
@Service
public class TimetableServiceImpl implements TimetableService<TimetableModel> {
    
    private TimetableRepository timetableDao;
    
    @Autowired
    public TimetableServiceImpl(TimetableRepository timetableDao) {
        this.timetableDao = timetableDao;
    }
    
    @Override
    public void updateTimetable(TimetableModel timetableModel) throws ServiceException {
        TimetableEntity timetableEntity = new TimetableEntity();
        timetableEntity.setId(timetableModel.getId());
        CourseEntity courseEnity = new CourseEntity();
        courseEnity.setId(timetableModel.getCourse().getId());
        timetableEntity.setCourse(courseEnity);
        timetableEntity.setDescription(timetableModel.getDescription());
        timetableEntity.setEndTime(timetableModel.getEndTime());
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(timetableModel.getGroup().getId());
        timetableEntity.setGroup(groupEntity);
        timetableEntity.setStartTime(timetableModel.getStartTime());
        timetableEntity.setWeekDay(DayOfWeek.valueOf(timetableModel.getWeekDay().toString()));

        try {
            timetableDao.update(timetableEntity);
        } catch (RepositoryException e) {
            throw new ServiceException("Updating the timetable failed.", e);
        }
    }
}
