package com.guli.gulicoupon.service.impl;

import com.guli.gulicoupon.dao.SeckillSessionDao;
import com.guli.gulicoupon.entity.SeckillSessionEntity;
import com.guli.gulicoupon.entity.SeckillSkuRelationEntity;
import com.guli.gulicoupon.service.SeckillSessionService;
import com.guli.gulicoupon.service.SeckillSkuRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;


@Service("seckillSessionService")
public class SeckillSessionServiceImpl extends ServiceImpl<SeckillSessionDao, SeckillSessionEntity> implements SeckillSessionService {


    @Autowired
    SeckillSkuRelationService relationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {


        QueryWrapper<SeckillSessionEntity> queryWrapper = new QueryWrapper<>();
        String sessionId = (String) params.get("promotionSessionId");

        if (!StringUtils.isEmpty(sessionId)) {

            queryWrapper.eq("promotion_session_id", sessionId);

        }


        IPage<SeckillSessionEntity> page = this.page(
                new Query<SeckillSessionEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SeckillSessionEntity> getKilSkuLatest3Days() {
        List<SeckillSessionEntity> entityList = this.list(new QueryWrapper<SeckillSessionEntity>().between("start_time", startTime(), endTime()));
        if (entityList != null && entityList.size() > 0) {
            List<SeckillSessionEntity> collect = entityList.stream().map(itm -> {

                List<SeckillSkuRelationEntity> sessionId = relationService.list(new QueryWrapper<SeckillSkuRelationEntity>().eq("promotion_session_id", itm.getId()));
                itm.setRelationSkus(sessionId);
                return itm;
            }).collect(Collectors.toList());

            return collect;
        }
        return null;
    }


    private String startTime() {
        // 开始时间
        LocalDate now = LocalDate.now();
        //最小时间
        LocalTime localTime1 = LocalTime.MIN;
        LocalDateTime of = LocalDateTime.of(now, localTime1);
        String format = of.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return format;
    }


    private String endTime() {

        // 开始时间
        LocalDate now = LocalDate.now();
        // 加两天
        LocalDate localDate = now.plusDays(2);
        // 最大时间
        LocalTime localTime = LocalTime.MAX;

        LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
        String format = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


        return format;
    }


}