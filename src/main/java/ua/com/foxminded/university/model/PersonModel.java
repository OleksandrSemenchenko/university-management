package ua.com.foxminded.university.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonModel implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String firstName;
    private String lastName;
}
