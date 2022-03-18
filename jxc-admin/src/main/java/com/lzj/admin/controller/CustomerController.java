package com.lzj.admin.controller;


import com.lzj.admin.model.RespBean;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.query.CustomerQuery;
import com.lzj.admin.service.ICustomerService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author liehuo
 * @since 2022-03-08
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private ICustomerService customerService;

    @RequestMapping("index")
    public String index(){
        return "customer/customer";
    }
    @RequestMapping("addOrUpdateCustomerPage")
    public String addOrUpdateCustomerPage(Integer id, Model model){
        if (null != id){
            model.addAttribute("id",id);
            model.addAttribute("customer",customerService.getById(id));
        }
        return "customer/add_update";
    }
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(CustomerQuery customerQuery){
        return customerService.customerList(customerQuery);
    }
    @RequestMapping("save")
    @ResponseBody
    public RespBean saveCustomer(Customer customer){
        customerService.saveCustomer(customer);
        return RespBean.success("客户记录添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public RespBean updateCustomer(Customer customer){
        customerService.updateCustomer(customer);
        return RespBean.success("客户记录更新成功");
    }
    @RequestMapping("delete")
    @ResponseBody
    public RespBean deleteCustomer(Integer[] ids){
        customerService.deleteCustomer(ids);
        return RespBean.success("客户记录更新成功");
    }
}
