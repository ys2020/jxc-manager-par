package com.lzj.admin.service;

import com.lzj.admin.pojo.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lzj.admin.query.GoodsQuery;

import java.util.Map;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
public interface IGoodsService extends IService<Goods> {

    Map<String, Object> goodsList(GoodsQuery goodsQuery);

    void saveGoods(Goods goods);

    void updateGoods(Goods goods);

    void deleteGoods(Integer id);

    String genGoodsCode();

    void updateStock(Goods goods);

    void deleteStock(Integer id);
}
