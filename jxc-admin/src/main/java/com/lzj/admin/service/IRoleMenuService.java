package com.lzj.admin.service;

import com.lzj.admin.pojo.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-03
 */
public interface IRoleMenuService extends IService<RoleMenu> {


    List<Integer> midsByroleId(Integer id);

    List<String> findAuthoritiesByroleName(List<String> roleName);
}
