package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.mapper.RoleMapper;
import com.lzj.admin.pojo.RoleMenu;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.RoleQuery;
import com.lzj.admin.service.IRoleMenuService;
import com.lzj.admin.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-01
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Resource
    private IRoleMenuService menuService;
    @Override
    public Map<String, Object> roleList(RoleQuery roleQuery) {
        IPage<Role> page =new Page<>(roleQuery.getPage(),roleQuery.getLimit());
        QueryWrapper<Role> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("is_del",0);
        if (StringUtils.isNotBlank(roleQuery.getRoleName())){
            queryWrapper.like("role_name", roleQuery.getRoleName());
        }
        page=this.baseMapper.selectPage(page,queryWrapper);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()),"角色名不能为空");
        Role temp =this.findRoleByName(role.getName());
        AssertUtil.isTrue(null!=temp && temp.getId().equals(role.getId()),"角色名已存在");
        AssertUtil.isTrue(!(this.updateById(role)),"角色记录更新失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getName()),"用户名不能为空");
        AssertUtil.isTrue(null !=this.findRoleByName(role.getName()),"用户名已存在");
        role.setIsDel(0);
        AssertUtil.isTrue(!this.save(role),"用户记录添加失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteRole(Integer id) {
        AssertUtil.isTrue(null==id,"请选择要删除的角色");
        Role role =this.getById(id);
        AssertUtil.isTrue(null==role,"没有要删除的角色");
        AssertUtil.isTrue(!this.updateById(role),"角色记录删除失败");
    }

    @Override
    public Role findRoleByName(String name) {
        return this.baseMapper.selectOne(new QueryWrapper<Role>().eq("is_del",0).eq("name",name));
    }

    @Override
    public void addGrant(Integer roleId, Integer[] mids) {
        Role role =this.getById(roleId);
        AssertUtil.isTrue(null==role,"角色信息不存在!");
        int count =menuService.count(new QueryWrapper<RoleMenu>().eq("role_id",roleId));
        if (count>0){
            AssertUtil.isTrue(menuService.remove(new QueryWrapper<RoleMenu>().eq("role_id",roleId)),"角色授权失败");
        }
        if (null!=mids && mids.length>0){
            List<RoleMenu> list =new ArrayList<RoleMenu>();
            for (Integer mid : mids) {
                RoleMenu r =new RoleMenu();
                r.setRoleId(roleId);
                r.setMenuId(mid);
                list.add(r);
            }
            AssertUtil.isTrue(!(menuService.saveBatch(list)),"角色信息授权失败");
        }
    }

    @Override
    public List<Map<String, Object>> queryAllRoles(Integer userId) {
        return this.baseMapper.queryAllRoles(userId);
    }
}
