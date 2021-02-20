package com.guli.guliproduclt.dao;

import com.guli.guliproduclt.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
