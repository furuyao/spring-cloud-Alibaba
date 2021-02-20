package com.guli.gulimember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guli.common.utils.PageUtils;
import com.guli.gulimember.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * 会员收货地址
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 11:55:31
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
    *@Author: fry
    *@Description: 查询会员收获地址
    *@Param: [memeberId]
    *@Date: 2020/9/8 16:01
    */
    List<MemberReceiveAddressEntity> getAddress(Long memeberId);
}

