package com.lzj.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lzj.admin.pojo.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.CustomerQuery;

import java.util.Map;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-08
 */
public interface ICustomerService extends IService<Customer> {

    Map<String, Object> customerList(CustomerQuery customerQuery);

    void saveCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void chekeParam(Customer customer);
    Customer findCustomerByName(String name);

    void deleteCustomer(Integer[] ids);
}
