package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.AttrEntity;
import com.guli.guliproduclt.vo.AttVo;
import com.guli.guliproduclt.vo.AttrRespVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-15 22:29:44
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
    *@Author: fry
    *@Description: 保存分组信息加关联信息
    *@Param: [attr]
    *@Date: 2020/8/16 9:43
    */
    void saveAttr(AttVo attr);

    /**
    *@Author: fry
    *@Description: 分页查询加模糊查询
    *@Param: [params, catelogId]
    *@Date: 2020/8/16 10:35
    */
    PageUtils queryBsesPage(Map<String, Object> params, Long catelogId, String type);

    /**
    *@Author: fry
    *@Description: 回显查询
    *@Param: [attrId]
    *@Date: 2020/8/16 20:55
    */
    AttrRespVo getAttrInfo(Long attrId);

    /**
    *@Author: fry
    *@Description: 修改
    *@Param: [attr]
    *@Date: 2020/8/16 21:21
    */
    void updateAttr(AttrRespVo attr);

    /**
    *@Author: fry
    *@Description: 查询关联关系列表
    *@Param: [attrgroupId]
    *@Date: 2020/8/17 10:12
    */
    List<AttrEntity> getTolistAttr(Long attrgroupId);

    /**
    *@Author: fry
    *@Description: 查询分组可以绑定的属性
    *@Param: [attrgroupId, params]
    *@Date: 2020/8/17 12:33
    */
    PageUtils selectNolist(Long attrgroupId, Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 查询可以被索引的属性
    *@Param: [collect]
    *@Date: 2020/8/24 18:01
    */
    List<Long> selectSearAttrs(List<Long> collect);
}

