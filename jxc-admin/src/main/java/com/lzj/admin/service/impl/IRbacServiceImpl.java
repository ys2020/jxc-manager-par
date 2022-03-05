package com.lzj.admin.service.impl;

import com.lzj.admin.service.IRbacServier;
import com.lzj.admin.service.IRoleMenuService;
import com.lzj.admin.service.IUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
@Service
public class IRbacServiceImpl implements IRbacServier {
    @Resource
    private IUserRoleService userRoleService;
    @Resource
    private IRoleMenuService roleMenuService;
    @Override
    public List<String> findroleByUserName(String userName) {

        return  userRoleService.findroleByUserName(userName);
    }

    @Override
    public List<String> findAuthoritiesByroleName(List<String> roleName) {
        return roleMenuService.findAuthoritiesByroleName(roleName);
    }
}
