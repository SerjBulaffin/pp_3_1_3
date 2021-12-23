package com.kata.bootfetch.kata_fetch.service;

import com.kata.bootfetch.kata_fetch.entity.Role;
import com.kata.bootfetch.kata_fetch.repository.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleDao.getRoleByRole(name);
    }

    @Override
    public Role getRoleById(Long id) {
        return roleDao.findById(id).get();
    }

    @Override
    public List<Role> allRoles() {
        return roleDao.findAll();
    }

}
