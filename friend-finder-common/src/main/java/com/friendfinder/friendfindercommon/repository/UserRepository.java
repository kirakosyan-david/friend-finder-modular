package com.friendfinder.friendfindercommon.repository;

import com.friendfinder.friendfindercommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update `friend_finder`.`user` set `password` = :password where `id` = :id", nativeQuery = true)
    void updateUserPasswordById(@Param("password") String password, @Param("id") int id);

    Page<User> findByNameContainingIgnoreCaseOrSurnameContainingIgnoreCase(String name, String surname, Pageable pageable);

    Page<User> findUsersByIdIn(List<Integer> friendsId, Pageable pageable);

}
