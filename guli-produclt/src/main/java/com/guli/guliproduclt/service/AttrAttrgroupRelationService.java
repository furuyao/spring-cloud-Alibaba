package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.AttrAttrgroupRelationEntity;
import com.guli.guliproduclt.vo.AttrgroupRelationVo;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void deleteRelation(AttrgroupRelationVo... relationVos);


    void addRelation(List<AttrgroupRelationVo> vos);
}

