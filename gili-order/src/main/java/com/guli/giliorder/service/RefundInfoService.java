package com.guli.giliorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.giliorder.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:28:40
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

