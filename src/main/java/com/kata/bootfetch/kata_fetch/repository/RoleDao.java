package com.kata.bootfetch.kata_fetch.repository;

import com.kata.bootfetch.kata_fetch.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {
    Role getRoleByRole(String name);
//
//    Role getRoleById(Long id);
//
//    List<Role> getAllRoles();
}
