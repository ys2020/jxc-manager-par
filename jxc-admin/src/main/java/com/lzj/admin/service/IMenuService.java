package com.lzj.admin.service;

import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-03
 */
public interface IMenuService extends IService<Menu> {

    List<TreeDto>  queryAllMenus(Integer id);

    Menu findMenuByGradeAndName(String name,Integer id);
    Menu findMenuByPid(Integer pid);
    Menu findMenuByGradeAndUrl(Integer grade,String url);
    Menu findMenuByAclvalue(String acl);
    Map<String, Object> menuList();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void deteleMenu(Integer id);
}
