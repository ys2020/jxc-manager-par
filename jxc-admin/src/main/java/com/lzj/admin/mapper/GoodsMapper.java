package com.lzj.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lzj.admin.pojo.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lzj.admin.query.GoodsQuery;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
public interface GoodsMapper extends BaseMapper<Goods> {


    IPage<Goods> goodsList(IPage<Goods> page, @Param("goodsQuery") GoodsQuery goodsQuery);
}
