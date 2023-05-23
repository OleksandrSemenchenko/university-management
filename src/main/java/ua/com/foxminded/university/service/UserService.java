package ua.com.foxminded.university.service;

import java.util.List;

import ua.com.foxminded.university.dto.UserDTO;
import ua.com.foxminded.university.exception.ServiceException;

public interface UserService extends GenericService<UserDTO> {

    public UserDTO getByEmail(String user) throws ServiceException;

    public void deleteByEmail(String email) throws ServiceException;

    public List<UserDTO> getNotAuthorizedUsers() throws ServiceException;
}