package com.guli.gulicoupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.gulicoupon.entity.SeckillSessionEntity;

import java.util.List;
import java.util.Map;

/**
 * 秒杀活动场次
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 14:50:17
 */
public interface SeckillSessionService extends IService<SeckillSessionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 查询三天要掺加秒杀的活动
    *@Param: []
    *@Date: 2020/9/13 11:34
    */
    List<SeckillSessionEntity> getKilSkuLatest3Days();

}

