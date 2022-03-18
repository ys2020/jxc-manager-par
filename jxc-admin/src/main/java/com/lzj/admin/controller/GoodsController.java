package com.lzj.admin.controller;


import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IGoodsTypeService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    private IGoodsService goodsService;
    @Resource
    private IGoodsTypeService goodsTypeService;

    @RequestMapping("index")
    public String index(){
        return "goods/goods";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> goodsList(GoodsQuery goodsQuery){
        return goodsService.goodsList(goodsQuery);
    }

    @RequestMapping("addOrUpdateGoodsPage")
    public String addOrUpdateGoodsPage(Integer id, Integer typeId, Model model){
        if (null != id){
            Goods goods=goodsService.getById(id);
            model.addAttribute("goods",goods);
            model.addAttribute("goodsType",goodsTypeService.getById(goods.getTypeId()));
        }else {
          if (null != typeId){
              model.addAttribute("goodsType",goodsTypeService.getById(typeId));
          }
        }
        return "goods/add_update";
    }
    @RequestMapping("toGoodsTypePage")
    public String toGoodsTypePage(Integer typeId,Model model){
        if (null!=typeId){
            model.addAttribute("typeId",typeId);
        }
        return "goods/goods_type";
    }
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveGoods(Goods goods){
        goodsService.saveGoods(goods);
        return RespBean.success("商品添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean updateGoods(Goods goods){
        goodsService.updateGoods(goods);
        return RespBean.success("商品添加成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteGoods(Integer id){
        goodsService.deleteGoods(id);
        return RespBean.success("商品添加成功");
    }
}
