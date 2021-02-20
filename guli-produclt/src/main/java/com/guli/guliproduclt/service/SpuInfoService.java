package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.SpuInfoDescEntity;
import com.guli.guliproduclt.entity.SpuInfoEntity;
import com.guli.guliproduclt.vo.SpuSaveVo;

import java.util.List;
import java.util.Map;

/**
 * spu信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 保存上架商品
    *@Param: [spuInfo]
    *@Date: 2020/8/19 9:08
    */
    void saveSpuinfo(SpuSaveVo spuInfo);

    /**
    *@Author: fry
    *@Description: 保存图片描述
    *@Param: [decript]
    *@Date: 2020/8/19 9:47
    */
    void saveInfoDecript(SpuInfoDescEntity decript);

    /**
    *@Author: fry
    *@Description: 搜索
    *@Param: [params]
    *@Date: 2020/8/20 9:48
    */
    PageUtils queryPageList(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 上架商品保存进ES
    *@Param: [spuId]
    *@Date: 2020/8/24 11:17
    */
    void spuIdsaveEs(Long spuId);

    /**
    *@Author: fry
    *@Description: 根据SKU查SOPU
    *@Param: [skuId]
    *@Date: 2020/9/9 20:45
    */
    SpuInfoEntity getSpuInfoById(Long skuId);
}

