package com.lzj.admin.controller;


import com.lzj.admin.dto.TreeDto;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.GoodsType;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.IGoodsTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品类别表 前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
@Controller
@RequestMapping("/goodsType")
public class GoodsTypeController {
    @Resource
    private IGoodsTypeService goodsTypeService;

    @RequestMapping("index")
    public String index(){
        return "goodsType/goods_type";
    }
    @RequestMapping("addGoodsTypePage")
    public String addGoodsTypePage(Integer pId,Model model){
        model.addAttribute("pId",pId);
        return "goodsType/add";
    }

    @RequestMapping("queryAllGoodsTypes")
    @ResponseBody
    public List<TreeDto> queryAllGoodsTypes(Integer typeId){
        return goodsTypeService.queryAllGoodsTypes(typeId);
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> goodsTypeList(){
        return goodsTypeService.goodsTypeList();
    }

    @RequestMapping("save")
    @ResponseBody
    public RespBean saveGoodsType(GoodsType goodsType){
        goodsTypeService.saveGoodsType(goodsType);
        return RespBean.success("类别添加成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteGoodsType(Integer id){
        goodsTypeService.deleteGoodsType(id);
        return RespBean.success("类别添加成功");
    }
}
