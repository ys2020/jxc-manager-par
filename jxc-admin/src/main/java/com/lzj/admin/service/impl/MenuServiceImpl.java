package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;
import com.lzj.admin.mapper.MenuMapper;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.pojo.RoleMenu;
import com.lzj.admin.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.IRoleMenuService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-03
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Resource
    private IRoleMenuService roleMenuService;
    @Override
    public List<TreeDto> queryAllMenus(Integer id) {
        List<TreeDto> list=this.baseMapper.queryAllMenus();
        List<Integer> roleHasMids =roleMenuService.midsByroleId(id);
        if (CollectionUtils.isNotEmpty(roleHasMids)){
            list.forEach(l ->{
                if (roleHasMids.contains(l.getId())){
                    l.setChecked(true);
                }
                    });
        }
        return list;
    }

    @Override
    public Menu findMenuByGradeAndName(String name, Integer grade) {
        return this.getOne(new QueryWrapper<Menu>().eq("name",name).eq("grade",grade).eq("is_del",0));
    }

    @Override
    public Menu findMenuByPid(Integer pid) {
        return this.getOne(new QueryWrapper<Menu>().eq("id",pid).eq("is_del",0));
    }

    @Override
    public Menu findMenuByGradeAndUrl(Integer grade, String url) {
        return this.getOne(new QueryWrapper<Menu>().eq("is_del",0).eq("grade",grade).eq("url",url));
    }

    @Override
    public Menu findMenuByAclvalue(String acl) {
        return this.getOne(new QueryWrapper<Menu>().eq("is_del",0).eq("acl_value",acl));
    }

    @Override
    public Map<String, Object> menuList() {
        List<Menu> list =this.list(new QueryWrapper<Menu>().eq("is_del",0));
        return PageResultUtil.getResult((long) list.size(),list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveMenu(Menu menu) {
        /**
         * 1.菜单名非空 且同级菜单名不饿重复
         * 2.上级菜单非空
         * 3.acl_value 权限码非空唯一
         * 4.url不能重复
         * 5.菜单只有 一二三级菜单
         * 6.参数验证
         */
        AssertUtil.isTrue(StringUtils.isBlank(menu.getName()),"菜单名不可为空");
        if(menu.getGrade()==1){
            AssertUtil.isTrue(null!=this.findMenuByGradeAndName(menu.getName(),menu.getGrade()),"该层级下已存在此菜单名");
        }
        AssertUtil.isTrue(null==menu.getGrade() || !(menu.getGrade()==1 || menu.getGrade()==2 || menu.getGrade()==0),"该层级不合法");
        AssertUtil.isTrue(null==menu.getPId() || null==this.findMenuByPid(menu.getPId()),"请指定上级菜单");
        AssertUtil.isTrue(null!=this.findMenuByAclvalue(menu.getAclValue()),"权限码已存在");
        AssertUtil.isTrue(null!=this.findMenuByGradeAndUrl(menu.getGrade(),menu.getUrl()),"Url不可重复");
        menu.setIsDel(0);
        menu.setState(0);
        AssertUtil.isTrue(!this.save(menu),"菜单记录添加是失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateMenu(Menu menu) {
        AssertUtil.isTrue(null==this.findMenuByPid(menu.getId()),"该菜单记录不存在");
        AssertUtil.isTrue(StringUtils.isBlank(menu.getName()),"菜单名不可为空");
        Menu temp=this.findMenuByPid(menu.getId());
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(menu.getId())),"已存在此菜单名");
        AssertUtil.isTrue(null==temp.getPId() || null==this.findMenuByPid(menu.getPId()),"请指定上级菜单");
        AssertUtil.isTrue(null==menu.getGrade() || !(menu.getGrade()==1 || menu.getGrade()==2 || menu.getGrade()==0),"该层级不合法");
        temp =this.findMenuByAclvalue(menu.getAclValue());
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(menu.getId())),"权限码已存在");
        if(menu.getGrade()==1){
            temp =this.findMenuByGradeAndUrl(menu.getGrade(),menu.getUrl());
            AssertUtil.isTrue(null!=temp && !(temp.getId().equals(menu.getId())),"Url不可重复");
        }

        AssertUtil.isTrue(!this.updateById(menu),"菜单记录更新是失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deteleMenu(Integer id) {
        /**
         * 1.该记录是否存在
         * 2.该记录下是否有子菜单
         * 3.关联角色授权删除
         * 4.执行删除操作
         */
        Menu menu=this.findMenuByPid(id);
        AssertUtil.isTrue(null==id || null==menu.getId(),"待删除的记录不存在");
        int count =this.count(new QueryWrapper<Menu>().eq("is_del",0).eq("p_id",id));
        AssertUtil.isTrue(count>0,"该记录存在子菜单无法删除");
        count =roleMenuService.count(new QueryWrapper<RoleMenu>().eq("menu_id",id));
        if (count>0){
            AssertUtil.isTrue(!roleMenuService.remove(new QueryWrapper<RoleMenu>().eq("menu_id",id)),"菜单删除失败");
        }
        menu.setIsDel(1);
        AssertUtil.isTrue(!this.updateById(menu),"菜单记录删除失败");
    }


}
