package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.AttrGroupEntity;
import com.guli.guliproduclt.vo.AttrGroupWithAttrsVo;
import com.guli.guliproduclt.vo.SkuItemVo;
import com.guli.guliproduclt.vo.SpuItemAttrGroupVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPage(Map<String, Object> params, long attrgrop);

    List<AttrGroupWithAttrsVo> selectBycatelogIdList(Long catelogId);

    List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long skuId, Long catalogId);
}

