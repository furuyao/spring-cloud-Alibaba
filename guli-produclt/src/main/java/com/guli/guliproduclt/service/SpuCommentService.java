package com.guli.guliproduclt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliproduclt.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 10:39:18
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

