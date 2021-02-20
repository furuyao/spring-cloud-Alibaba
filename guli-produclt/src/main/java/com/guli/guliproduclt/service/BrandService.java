package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.BrandEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void updateClassification(BrandEntity brand);

}

