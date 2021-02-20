package com.guli.gulicoupon.service.impl;

import com.guli.gulicoupon.dao.SeckillSkuRelationDao;
import com.guli.gulicoupon.entity.SeckillSessionEntity;
import com.guli.gulicoupon.entity.SeckillSkuRelationEntity;
import com.guli.gulicoupon.service.SeckillSkuRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;


@Service("seckillSkuRelationService")
public class SeckillSkuRelationServiceImpl extends ServiceImpl<SeckillSkuRelationDao, SeckillSkuRelationEntity> implements SeckillSkuRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SeckillSkuRelationEntity> queryWrapper = new QueryWrapper<>();
        String sessionId = (String) params.get("promotionSessionId");

        if (!StringUtils.isEmpty(sessionId)) {

            queryWrapper.eq("promotion_session_id", sessionId);

        }


        IPage<SeckillSkuRelationEntity> page = this.page(
                new Query<SeckillSkuRelationEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

}