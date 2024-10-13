package com.task.thinkon.repository;

import com.task.thinkon.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE (u.email = :email OR u.username = :username OR u.phoneNumber = :phoneNumber) "
            + "AND (:userId IS NULL OR u.id <> :userId)")
    List<User> findByEmailOrUsernameOrPhoneNumberAndIdNot(@Param("email") String email,
                                                          @Param("username") String username,
                                                          @Param("phoneNumber") String phoneNumber,
                                                          @Param("userId") Long userId);
}
