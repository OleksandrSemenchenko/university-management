package ua.com.foxminded.university.dto;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Integer id;
    
    @ToString.Exclude
    private UserDTO user;
    
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<LessonDTO> lessons;
    
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CourseDTO> courses;
}