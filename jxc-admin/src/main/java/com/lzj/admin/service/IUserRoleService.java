package com.lzj.admin.service;

import com.lzj.admin.pojo.UserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-04
 */
public interface IUserRoleService extends IService<UserRole> {

    List<String> findroleByUserName(String userName);


}
