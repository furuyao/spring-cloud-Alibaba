package com.guli.gulimember.dao;

import com.guli.gulimember.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-07 11:55:30
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
