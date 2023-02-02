package ua.com.foxminded.university.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.university.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    
    @Query("select u from UserEntity u where u.password != 'null'")
    public List<UserEntity> getAllHavingPassword();
    
    @Query("select u from UserEntity u join fetch u.authority "
         + "where u.email = :email and u.isActive = true")
    public UserEntity findActiveUserByEmail(@Param("email")String email);
}
