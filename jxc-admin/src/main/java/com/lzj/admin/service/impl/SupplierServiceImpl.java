package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Supplier;
import com.lzj.admin.mapper.SupplierMapper;
import com.lzj.admin.query.SupplierQuery;
import com.lzj.admin.service.ISupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-08
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {
    @Resource
    private ISupplierService supplierService;

    @Override
    public Map<String, Object> supplierList(SupplierQuery supplierQuery) {
        IPage<Supplier> page =new Page<Supplier>(supplierQuery.getPage(),supplierQuery.getLimit());
        QueryWrapper<Supplier> queryWrapper =new QueryWrapper<Supplier>();
        queryWrapper.eq("is_del",0);
        if (!StringUtils.isBlank(supplierQuery.getSupplierName())){
            queryWrapper.like("name",supplierQuery.getSupplierName());
        }
        page=this.baseMapper.selectPage(page,queryWrapper);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    public void checkParam(Supplier supplier) {
        AssertUtil.isTrue(null==supplier.getName(),"供应商名称不能为空");
        AssertUtil.isTrue(null==supplier.getNumber(),"供应商手机号不能为空");
        AssertUtil.isTrue(null==supplier.getContact(),"供应商联系人不能为空");
        AssertUtil.isTrue(null==supplier.getAddress(),"供应商地址不能为空");
    }

    @Override
    public Supplier findSupplierByName(String name) {
        return this.getOne(new QueryWrapper<Supplier>().eq("name",name).eq("is_del",0));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveSupplier(Supplier supplier) {
        checkParam(supplier);
        AssertUtil.isTrue(null!=this.findSupplierByName(supplier.getName()),"供应商已存在");
        supplier.setIsDel(0);
        AssertUtil.isTrue(!(this.save(supplier)),"供应商记录添加失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateSupplier(Supplier supplier) {
        AssertUtil.isTrue(null==supplierService.getById(supplier.getId()),"请选择供应商记录");
        checkParam(supplier);
        Supplier temp =this.findSupplierByName(supplier.getName());
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(supplier.getId())),"供应商已存在");
        AssertUtil.isTrue(!(this.updateById(supplier)),"供应商记录添加失败");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteSupplier(Integer[] ids) {
        AssertUtil.isTrue(null==ids || ids.length==0,"请选择要删除的数据");
        List<Supplier> list =new ArrayList<Supplier>();
        for (Integer id : ids) {
            Supplier temp =this.getById(id);
            temp.setIsDel(1);
            list.add(temp);
        }
        AssertUtil.isTrue(!(this.updateBatchById(list)),"供应商记录删除失败");
    }
}
