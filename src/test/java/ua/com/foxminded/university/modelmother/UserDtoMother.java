package ua.com.foxminded.university.modelmother;

import ua.com.foxminded.university.dto.UserDTO;

public class UserDtoMother {
    
    public static final String PASSWORD = "pass";
    public static final String EMAIL = "my@email";
    
    public static UserDTO.UserDTOBuilder complete() {
        return UserDTO.builder().email(EMAIL)
                                .enabled(true)
                                .password(PASSWORD);
    }
}
