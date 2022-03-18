package com.lzj.admin.service;

import com.lzj.admin.pojo.Supplier;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.SupplierQuery;

import java.util.Map;

/**
 * <p>
 * 供应商表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-08
 */
public interface ISupplierService extends IService<Supplier> {

    Map<String, Object> supplierList(SupplierQuery supplierQuery);

    void saveSupplier(Supplier supplier);

    void checkParam(Supplier supplier);

    Supplier findSupplierByName(String name);

    void updateSupplier(Supplier supplier);

    void deleteSupplier(Integer[] ids);
}
