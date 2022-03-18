package com.lzj.admin.service.impl;

import com.lzj.admin.pojo.UserRole;
import com.lzj.admin.mapper.UserRoleMapper;
import com.lzj.admin.service.IUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-04
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {

    @Override
    public List<String> findroleByUserName(String userName) {
       return this.baseMapper.findroleByUserName(userName);
    }
}
