package com.guli.guliproduclt.service.impl;

import com.guli.guliproduclt.entity.AttrEntity;
import com.guli.guliproduclt.service.AttrService;
import com.guli.guliproduclt.vo.AttrGroupWithAttrsVo;
import com.guli.guliproduclt.vo.SkuItemVo;
import com.guli.guliproduclt.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliproduclt.dao.AttrGroupDao;
import com.guli.guliproduclt.entity.AttrGroupEntity;
import com.guli.guliproduclt.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, long attrgrop) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> obj.eq("attr_group_id", key).or().like("attr_group_name", key));
        }

        if (attrgrop == 0) {
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);

        } else {
         wrapper.eq("catelog_id",attrgrop);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper
            );
            return new PageUtils(page);
        }

    }
    @Override
    public List<AttrGroupWithAttrsVo> selectBycatelogIdList(Long catelogId) {

        List<AttrGroupEntity> entityList = this.list(
                new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrsVo> collect = entityList.stream().map(itm -> {

            AttrGroupWithAttrsVo attrGroupEntity = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(itm, attrGroupEntity);
            List<AttrEntity> tolistAttr = attrService.getTolistAttr(itm.getAttrGroupId());
            attrGroupEntity.setAttrs(tolistAttr);
            return attrGroupEntity;
        }).collect(Collectors.toList());


        return collect;
    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long skuId, Long catalogId) {


        AttrGroupDao baseMapper = this.getBaseMapper();

        List<SpuItemAttrGroupVo> attrGroupWih= baseMapper.getAttrGroupWih(skuId,catalogId);

        return attrGroupWih;
    }

}