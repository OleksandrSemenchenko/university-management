package ua.com.foxminded.university.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@NamedEntityGraph(name = "tipetableListOfCourse", attributeNodes = { 
        @NamedAttributeNode("id"),
        @NamedAttributeNode("name"), 
        @NamedAttributeNode("description"), 
        @NamedAttributeNode("timetableList")})
@Entity
@Table(name = "courses")
@Data
public class CourseEntity implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id")
    private TeacherEntity teacher;
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "course")
    private List<TimetableEntity> timetableList;
}