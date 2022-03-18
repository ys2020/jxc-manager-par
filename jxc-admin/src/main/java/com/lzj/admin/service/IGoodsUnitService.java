package com.lzj.admin.service;

import com.lzj.admin.pojo.Customer;
import com.lzj.admin.pojo.GoodsUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.GoodsUnitQuery;

import java.util.Map;

/**
 * <p>
 * 商品单位表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
public interface IGoodsUnitService extends IService<GoodsUnit> {

    Map<String, Object> unitList(GoodsUnitQuery goodsUnitQuery);

    void saveGoodsUnit(GoodsUnit goodsUnit);

    void updateGoodsUnit(GoodsUnit goodsUnit);

    void deleteGoodsUnit(Integer[] ids);

    GoodsUnit findUnitById(String name);
}
