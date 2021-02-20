package com.guli.guliproduclt.dao;

import com.guli.guliproduclt.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-08-15 22:29:44
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    List<Long> selectListById(@Param("collect") List<Long> collect);
}
