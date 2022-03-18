package com.lzj.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzj.admin.pojo.Customer;
import com.lzj.admin.pojo.Goods;
import com.lzj.admin.pojo.GoodsUnit;
import com.lzj.admin.mapper.GoodsUnitMapper;
import com.lzj.admin.query.GoodsUnitQuery;
import com.lzj.admin.service.IGoodsService;
import com.lzj.admin.service.IGoodsUnitService;
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
 * 商品单位表 服务实现类
 * </p>
 *
 * @author liehuo
 * @since 2022-03-14
 */
@Service
public class GoodsUnitServiceImpl extends ServiceImpl<GoodsUnitMapper, GoodsUnit> implements IGoodsUnitService {
    @Resource
    private IGoodsService goodsService;
    @Override
    public Map<String, Object> unitList(GoodsUnitQuery goodsUnitQuery) {
        IPage<GoodsUnit> page =new Page<GoodsUnit>(goodsUnitQuery.getPage(),goodsUnitQuery.getLimit());
        QueryWrapper<GoodsUnit> queryWrapper =new QueryWrapper<GoodsUnit>();
        page =this.baseMapper.selectPage(page,queryWrapper);
        return PageResultUtil.getResult(page.getTotal(),page.getRecords());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void saveGoodsUnit(GoodsUnit goodsUnit) {
        AssertUtil.isTrue(null==goodsUnit.getName(),"商品单位不能为空");
        GoodsUnit temp =this.findUnitById(goodsUnit.getName());
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(goodsUnit.getId())),"已存在相同单位");
        AssertUtil.isTrue(!(this.save(goodsUnit)),"商品单位添加成功！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void updateGoodsUnit(GoodsUnit goodsUnit) {
        GoodsUnit temp =this.findUnitById(goodsUnit.getName());
        AssertUtil.isTrue(null!=temp && !(temp.getId().equals(goodsUnit.getId())),"已存在相同单位");
        AssertUtil.isTrue(!(this.updateById(goodsUnit)),"商品单位更新成功！");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public void deleteGoodsUnit(Integer[] ids) {
        AssertUtil.isTrue(null==ids || ids.length==0,"请选择要删除的商品单位");
        List<Integer> list  =new ArrayList<Integer>();
        for (Integer id : ids) {
            int count =goodsService.count(new QueryWrapper<Goods>().eq("unit",id));
            AssertUtil.isTrue(count>0,"已有商品使用该单位，请更改或删除后再操作");
            GoodsUnit temp =this.getById(id);
            list.add(temp.getId());
        }
        AssertUtil.isTrue(!(this.removeByIds(list)),"商品单位删除失败");
    }

    @Override
    public GoodsUnit findUnitById(String name) {
        return this.baseMapper.selectOne(new QueryWrapper<GoodsUnit>().eq("name",name));

    }

}
