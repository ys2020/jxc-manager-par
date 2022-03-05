package com.lzj.admin.controller;

import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Role;
import com.lzj.admin.pojo.RoleMenu;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.RoleQuery;
import com.lzj.admin.service.IRoleMenuService;
import com.lzj.admin.service.IRoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService iRoleService;


    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> roleList(RoleQuery roleQuery){
        return iRoleService.roleList(roleQuery);
    }
    //新增角色
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(Role role){
        iRoleService.saveRole(role);
        return RespBean.success("用户记录添加成功");
    }
    //修改角色信息
    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Role role){
        iRoleService.updateRole(role);
        return RespBean.success("用户记录添加成功");
    }
    //删除角色
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        iRoleService.deleteRole(id);
        return RespBean.success("用户记录删除成功");
    }
    @RequestMapping("addOrUpdateRolePage")
    public String addOrUpdateUserPage(Integer id, Model model){
        if (null != id){
            model.addAttribute("role",iRoleService.getById(id));
        }
        return "role/add_update";
    }
    @RequestMapping("add_update")
    public String add_update(){
        return "role/add_update";
    }

    @RequestMapping("toAddGrantPage")
    public String grant(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return iRoleService.queryAllRoles(userId);
    }
    @RequestMapping("addGrant")
    @ResponseBody
    public RespBean addGrant(Integer roleId,Integer[] mids){
        iRoleService.addGrant(roleId,mids);
        return RespBean.success("角色授权成功");
    }
}
