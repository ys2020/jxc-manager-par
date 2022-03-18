package com.lzj.admin.controller;


import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.pojo.GoodsUnit;
import com.lzj.admin.query.CustomerQuery;
import com.lzj.admin.query.GoodsUnitQuery;
import com.lzj.admin.service.IGoodsUnitService;

import io.swagger.models.auth.In;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品单位表 前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
@Controller
@RequestMapping("/goodsUnit")
public class GoodsUnitController {
    @Resource
    private IGoodsUnitService goodsUnitService;

    @RequestMapping("allGoodsUnits")
    @ResponseBody
    public List<GoodsUnit> allGoodsUnits(){
        return goodsUnitService.list();
    }

    @RequestMapping("index")
    public String index(){
        return "goodsUnit/goodsUnit";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(GoodsUnitQuery goodsUnitQuery){
        return goodsUnitService.unitList(goodsUnitQuery);
    }

    @RequestMapping("addOrUpdateGoodsUnitPage")
    public String addOrUpdateGoodsUnitPage(Integer id, Model model){
        model.addAttribute("goodsUnit",goodsUnitService.getById(id));
        return "goodsUnit/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveCustomer(GoodsUnit goodsUnit){
        goodsUnitService.saveGoodsUnit(goodsUnit);
        return RespBean.success("客户记录添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean updateCustomer(GoodsUnit goodsUnit){
        goodsUnitService.updateGoodsUnit(goodsUnit);
        return RespBean.success("客户记录更新成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteCustomer(Integer[] ids){
        goodsUnitService.deleteGoodsUnit(ids);
        return RespBean.success("客户记录更新成功");
    }
}
