package com.guli.gulike.secki.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/13
 */
@EnableScheduling // 定时调度任务
@Configuration
@EnableAsync // 异步任务开启
public class ScheduledConfig {
}
