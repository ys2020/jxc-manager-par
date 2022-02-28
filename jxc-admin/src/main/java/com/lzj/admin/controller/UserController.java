package com.lzj.admin.controller;


import com.lzj.admin.exceptions.ParamsException;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.User;
import com.lzj.admin.query.UserQuery;
import com.lzj.admin.service.IUserService;
import com.lzj.admin.utils.AssertUtil;
import com.sun.xml.internal.ws.resources.HttpserverMessages;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author 老李
 */
@Controller
@RequestMapping("/user")
public class UserController {


    @Resource
    private IUserService userService;

   /* @RequestMapping("login")
    @ResponseBody
    public RespBean login(String userName, String password, HttpSession session){
        try {
            User user = userService.login(userName,password);
            session.setAttribute("user",user);
            return RespBean.success("用户登录成功!");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMsg());
        }catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("用户登录失败!");
        }
    }
*/

//    @RequestMapping("login")
//    @ResponseBody
//    public RespBean login(String userName, String password, HttpSession session){
//        User user = userService.login(userName,password);
//        session.setAttribute("user",user);
//        return RespBean.success("用户登录成功!");
//    }



    /**
     * 用户信息设置页面
     * @return
     */
    @RequestMapping("setting")
    public String setting(Principal principal, Model model){
        User user = userService.findUserByUserName(principal.getName());
        model.addAttribute("user",user);
        return "user/setting";
    }
    @RequestMapping("index")
    public String user(){
        return "user/user";
    }
    @RequestMapping("add_update")
    public String add_update(){
        return "user/add_update";
    }

    /**
     * 用户信息更新
     * @param user
     * @return
     */
    @RequestMapping("updateUserInfo")
    @ResponseBody
    public RespBean updateUserInfo(User user){
        try {
            userService.updateUserInfo(user);
            return RespBean.success("用户信息更新成功");
        } catch (ParamsException e) {
            e.printStackTrace();
            return RespBean.error(e.getMsg());
        }catch (Exception e) {
            e.printStackTrace();
            return RespBean.error("用户信息更新失败!");
        }
    }


    /**
     * 用户密码更新页
     * @return
     */
    @RequestMapping("password")
    public String password(){
        return "user/password";
    }




    /**
     * 用户密码更新

     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     * @return
     */
    @RequestMapping("updateUserPassword")
    @ResponseBody
    public RespBean updateUserPassword(Principal principal, String oldPassword, String newPassword, String confirmPassword){


            userService.updateUserPassword(principal.getName(),oldPassword,newPassword,confirmPassword);
            return RespBean.success("用户密码更新成功");

    }

    /**
     * 用户列表查询
     * @param userQuery
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> userList(UserQuery userQuery){
    return userService.userLlist(userQuery);
    }

    /**
     * 用户保存和编辑
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("addOrUpdateUserPage")
    public String addOrUpdateUserPage(Integer id,Model model){
        if (null != id){
            model.addAttribute("user",userService.getById(id));
        }
        return "user/add_update";
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    @RequestMapping("save")
    @ResponseBody
    public RespBean save(User user){
        userService.saveUser(user);
        return RespBean.success("用户记录添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean update(User user){
        userService.updateUser(user);
        return RespBean.success("用户记录添加成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer[] ids){
        userService.deleteUser(ids);
        return RespBean.success("用户记录删除成功");
    }
}
