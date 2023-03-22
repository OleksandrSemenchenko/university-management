package ua.com.foxminded.university.service.impl;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ConfigurationException;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ua.com.foxminded.university.entity.TimetableEntity;
import ua.com.foxminded.university.exception.ServiceException;
import ua.com.foxminded.university.model.TimetableModel;
import ua.com.foxminded.university.repository.TimetableRepository;
import ua.com.foxminded.university.service.TimetableService;


@Service
@Transactional
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService<TimetableModel> {
    
    private final TimetableRepository timetableRepository;
    
    @Override
    public List<TimetableModel> getAllTimetables() throws ServiceException {
        try {
            List<TimetableEntity> timetableEntities = timetableRepository.findAll();
            Type listType = new TypeToken<List<TimetableModel>>() {}.getType();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            return modelMapper.map(timetableEntities, listType);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Getting all timetables was failed", e);
        }
    }
    
    @Override
    public void updateTimetable(TimetableModel timetableModel) throws ServiceException {
        try {
            ModelMapper modelMapper = new ModelMapper();
            TimetableEntity timetableEntity = modelMapper.map(timetableModel, TimetableEntity.class);
            timetableRepository.save(timetableEntity);
        } catch (IllegalArgumentException | ConfigurationException | MappingException e) {
            throw new ServiceException("Updating the timetable failed.", e);
        }
    }
}
