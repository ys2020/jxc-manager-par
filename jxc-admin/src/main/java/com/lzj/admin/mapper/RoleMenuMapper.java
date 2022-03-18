package com.lzj.admin.mapper;

import com.lzj.admin.pojo.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色菜单表 Mapper 接口
 * </p>
 *
 * @author liehuo
 * @since 2022-03-03
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<Integer> midsByroleId(Integer id);

    List<String> findAuthoritiesByroleName(List<String> roleName);
}
