package com.lzj.admin.controller;


import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.query.SupplierQuery;
import com.lzj.admin.service.ISupplierService;
import io.swagger.models.auth.In;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 供应商表 前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2022-03-08
 */
@Controller
@RequestMapping("/supplier")
public class SupplierController {
    @Resource
    private ISupplierService supplierService;
@RequestMapping("addOrUpdateSupplierPage")
public String addOrUpdateSupplierPage(Integer id, Model model){
if (null!=id){
    model.addAttribute("id",id);
    model.addAttribute("supplier",supplierService.getById(id));
}
return "supplier/add_update";
}
@RequestMapping("index")
public String supplier(){
        return "supplier/supplier";
}
@RequestMapping("list")
@ResponseBody
public Map<String, Object> list(SupplierQuery supplierQuery){
        return supplierService.supplierList(supplierQuery);
}
@RequestMapping("save")
@ResponseBody
public RespBean saveSupplier(Supplier supplier){
    supplierService.saveSupplier(supplier);
    return RespBean.success("供应商记录添加成功");
}
@RequestMapping("update")
@ResponseBody
public RespBean updateSupplier(Supplier supplier){
    supplierService.updateSupplier(supplier);
    return RespBean.success("供应商更新添加成功");
}
@RequestMapping("delete")
@ResponseBody
public RespBean deleteSupplier(Integer[] ids){
    supplierService.deleteSupplier(ids);
    return RespBean.success("供应商删除添加成功");
}
}
