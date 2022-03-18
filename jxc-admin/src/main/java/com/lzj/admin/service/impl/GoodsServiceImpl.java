package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.mapper.GoodsMapper;
import com.lzj.admin.query.GoodsQuery;
import com.lzj.admin.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lzj.admin.service.IGoodsTypeService;
import com.lzj.admin.utils.AssertUtil;
import com.lzj.admin.utils.PageResultUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {
    @Resource
    private IGoodsTypeService goodsTypeService;
    @Override
    public Map<String, Object> goodsList(GoodsQuery goodsQuery) {
        IPage<Goods> page =new Page<Goods>(goodsQuery.getPage(),goodsQuery.getLimit());
        if (null!=goodsQuery.getTypeId()){
            goodsQuery.setTypeIds(goodsTypeService.queryAllGoodsTypesById(goodsQuery.getTypeId()));
        }
        page=this.baseMapper.goodsList(page,goodsQuery);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveGoods(Goods goods) {
        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()),"请指定商品名称!");
        AssertUtil.isTrue(null == goods.getTypeId(),"请指定商品类别!");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()),"请指定商品单位!");
        goods.setCode(genGoodsCode());
        goods.setInventoryQuantity(0);
        goods.setState(0);
        goods.setLastPurchasingPrice(0F);
        goods.setIsDel(0);
        AssertUtil.isTrue(!(this.save(goods)),"商品记录添加失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateGoods(Goods goods) {
        AssertUtil.isTrue(StringUtils.isBlank(goods.getName()),"请指定商品名称!");
        AssertUtil.isTrue(null == goods.getTypeId(),"请指定商品类别!");
        AssertUtil.isTrue(StringUtils.isBlank(goods.getUnit()),"请指定商品单位!");
        AssertUtil.isTrue(!(this.updateById(goods)),"商品记录更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteGoods(Integer id) {
         Goods good =this.getById(id);
        AssertUtil.isTrue(null==good,"不存在待删除的商品!");
        AssertUtil.isTrue(good.getState() == 1,"该商品已入库!");
        AssertUtil.isTrue(good.getState() == 2,"该商品已产生单据");
        good.setIsDel(1);
        AssertUtil.isTrue(!(this.updateById(good)),"商品记录删除失败");
    }

    @Override
    public String genGoodsCode() {
        String maxGoodsCode=this.baseMapper.selectOne(new QueryWrapper<Goods>().select("max(code) as code")).getCode();
        if(StringUtils.isNotEmpty(maxGoodsCode)){
            Integer code = Integer.valueOf(maxGoodsCode)+1;
            String codes = code.toString();
            int length = codes.length();
            for (int i = 4; i > length; i--) {
                codes = "0"+codes;
            }
            return codes;
        }else{
            return "0001";
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateStock(Goods goods) {
        /**
         * 商品库存量>0
         * 商品成本价>0
         */
        Goods temp =this.getById(goods.getId());
        AssertUtil.isTrue(null == goods,"待更新的商品记录不存在!");
        AssertUtil.isTrue(goods.getInventoryQuantity()<=0,"库存量必须>0");
        AssertUtil.isTrue(goods.getPurchasingPrice()<=0,"成本价必须>0");
        AssertUtil.isTrue(!(this.updateById(goods)),"商品更新失败!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteStock(Integer id) {
        Goods temp =this.getById(id);
        AssertUtil.isTrue(null == temp,"待更新的商品记录不存在!");
        AssertUtil.isTrue(temp.getState() == 2,"该商品已经发生单据，不可删除!");
        temp.setInventoryQuantity(0);
        AssertUtil.isTrue(!(this.updateById(temp)),"商品删除失败!");
    }
}
