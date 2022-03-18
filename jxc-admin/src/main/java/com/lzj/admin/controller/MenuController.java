package com.lzj.admin.controller;


import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Menu;
import com.lzj.admin.service.IMenuService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2022-03-03
 */
@Controller
@RequestMapping("/menu")
public class MenuController {
    @Resource
    private IMenuService iMenuService;


    @RequestMapping("queryAllMenus")
    @ResponseBody
 public List<TreeDto> queryAllMenus(Integer roleId){
     return iMenuService.queryAllMenus(roleId);
 }
 @RequestMapping("index")
 public String index(){
        return "menu/menu";
 }


@RequestMapping("updateMenuPage")
public String update(Integer id,Model model){
        model.addAttribute("menu",iMenuService.findMenuByPid(id));
    return "menu/update";
}

    @RequestMapping("list")
    @ResponseBody
 public Map<String,Object> list(){
        return iMenuService.menuList();
 }
@RequestMapping("addMenuPage")
public String addMenuPage(Integer grade,Integer pId, Model model) {
        model.addAttribute("grade",grade);
        model.addAttribute("pId",pId);
    return "menu/add";
}

    @RequestMapping("save")
    @ResponseBody
 public RespBean saveMenu(Menu menu){
        iMenuService.saveMenu(menu);
        return RespBean.success("菜单记录添加成功");
 }
    @RequestMapping("update")
    @ResponseBody
    public RespBean update(Menu menu){
        iMenuService.updateMenu(menu);
        return RespBean.success("菜单记录更新成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean delete(Integer id){
        iMenuService.deteleMenu(id);
        return RespBean.success("菜单记录删除成功");
    }
}
