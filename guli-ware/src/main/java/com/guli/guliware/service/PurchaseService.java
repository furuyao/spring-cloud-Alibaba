package com.guli.guliware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.guliware.entity.PurchaseEntity;
import com.guli.guliware.vo.MergeVo;
import com.guli.guliware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 12:33:31
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPagePurchase(Map<String, Object> params);

    void queryPageMerge(MergeVo params);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);
}

