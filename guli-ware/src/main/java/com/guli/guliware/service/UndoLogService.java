package com.guli.guliware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliware.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:33:31
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

