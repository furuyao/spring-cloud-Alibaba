package com.guli.gulicoupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.gulicoupon.entity.SeckillSessionEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 秒杀活动场次
 * 
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 14:50:17
 */
@Mapper
public interface SeckillSessionDao extends BaseMapper<SeckillSessionEntity> {
	
}
