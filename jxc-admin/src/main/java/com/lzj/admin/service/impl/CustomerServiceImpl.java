package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.mapper.CustomerMapper;
import com.lzj.admin.query.CustomerQuery;
import com.lzj.admin.service.ICustomerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import com.lzj.admin.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-08
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Override
    public Map<String, Object> customerList(CustomerQuery customerQuery) {
        IPage<Customer> page =new Page<Customer>(customerQuery.getPage(),customerQuery.getLimit());
        QueryWrapper<Customer> queryWrapper =new QueryWrapper<Customer>();
        queryWrapper.eq("is_del",0);
        if (null != customerQuery.getCustomerName()){
            queryWrapper.eq("name",customerQuery.getCustomerName());
        }
        page =this.baseMapper.selectPage(page,queryWrapper);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }
    @Override
    public void chekeParam(Customer customer) {
        AssertUtil.isTrue(null == customer.getName(),"客户名称不可为空");
        AssertUtil.isTrue(null == customer.getAddress(),"客户地址不可为空");
        AssertUtil.isTrue(null == customer.getContact(),"客户联系人不可为空");
        AssertUtil.isTrue(null == customer.getNumber(),"客户联系电话不可为空");
    }
    @Override
    public Customer findCustomerByName(String name) {
        return this.getOne(new QueryWrapper<Customer>().eq("name",name).eq("is_del",0));
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveCustomer(Customer customer) {
        chekeParam(customer);
        AssertUtil.isTrue(null != this.findCustomerByName(customer.getName()),"该客户已经存在");
        customer.setIsDel(0);
        AssertUtil.isTrue(!(this.save(customer)),"客户添加记录失败");
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateCustomer(Customer customer) {
        AssertUtil.isTrue(null ==this.getById(customer.getId()),"请选择客户记录");
        chekeParam(customer);
        Customer temp =this.findCustomerByName(customer.getName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(customer.getId())),"该客户已经存在");
        AssertUtil.isTrue(!(this.updateById(customer)),"客户添加记录失败");
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteCustomer(Integer[] ids) {
    AssertUtil.isTrue(null==ids || ids.length==0,"请选择要删除的客户信息");
        List<Customer> list  =new ArrayList<Customer>();
        for (Integer id : ids) {
            Customer temp =this.getById(id);
            temp.setIsDel(1);
            list.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(list)),"客户记录删除失败");
    }
}
