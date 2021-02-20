package com.guli.gulicoupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guli.gulicoupon.entity.CouponEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-19 14:50:17
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
