package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.pojo.RoleMenu;
import com.lzj.admin.mapper.RoleMenuMapper;
import com.lzj.admin.service.IRoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 角色菜单表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-03
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IRoleMenuService {

    @Override
    public List<Integer> midsByroleId(Integer id) {
        return this.baseMapper.midsByroleId(id);
    }

    @Override
    public List<String> findAuthoritiesByroleName(List<String> roleName) {
        return this.baseMapper.findAuthoritiesByroleName(roleName);
    }
}
