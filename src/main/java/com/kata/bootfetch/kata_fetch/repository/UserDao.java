package com.kata.bootfetch.kata_fetch.repository;

import com.kata.bootfetch.kata_fetch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    public User getUserByEmail(String email);
    public User getUserByFirstName(String name);
//    List<User> getAllUsers();
//
//    User getUserByName(String name);
//    User getUserByEmail(String email);
//
//    void addUser(User user);
//    void updateUser(User user);
//    void removeUserById(Long id);
//    User getUser(Long id);

    //void addUserDB();
}
