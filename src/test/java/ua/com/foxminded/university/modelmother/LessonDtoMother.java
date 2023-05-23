package ua.com.foxminded.university.modelmother;

import static ua.com.foxminded.university.entitymother.LessonMother.*;

import ua.com.foxminded.university.dto.LessonDTO;

public class LessonDtoMother {

    public static final int FIRST_LESSON = 1;
    public static LessonDTO.LessonDTOBuilder complete() {
        return LessonDTO.builder().datestamp(DATE)
                                  .description(SCHEDULE_DESCRIPTION)
                                  .lessonOrder(FIRST_LESSON);
    }
}
