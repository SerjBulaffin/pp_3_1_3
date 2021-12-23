package com.kata.bootfetch.kata_fetch.service;

import com.kata.bootfetch.kata_fetch.entity.Role;

import java.util.List;

public interface RoleService {
    Role getRoleByName(String name);

    Role getRoleById(Long id);

    List<Role> allRoles();

}
