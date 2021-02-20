package com.guli.guliproduclt.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.guli.common.constant.ProductConstant;
import com.guli.common.utils.Page;
import com.guli.common.utils.PageHelper;
import com.guli.guliproduclt.dao.AttrAttrgroupRelationDao;
import com.guli.guliproduclt.dao.AttrDao;
import com.guli.guliproduclt.dao.AttrGroupDao;
import com.guli.guliproduclt.dao.CategoryDao;
import com.guli.guliproduclt.entity.AttrAttrgroupRelationEntity;
import com.guli.guliproduclt.entity.AttrEntity;
import com.guli.guliproduclt.entity.AttrGroupEntity;
import com.guli.guliproduclt.entity.CategoryEntity;
import com.guli.guliproduclt.service.AttrService;
import com.guli.guliproduclt.service.CategoryService;
import com.guli.guliproduclt.vo.AttVo;
import com.guli.guliproduclt.vo.AttrRespVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    AttrGroupDao attrGroupDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }


    @Transactional
    @Override
    public void saveAttr(AttVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        // 保存基本数据
        this.save(attrEntity);
        // 保存关联关系
        if (attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() && attr.getAttrGroupId() != null) {
            AttrAttrgroupRelationEntity attrAttr = new AttrAttrgroupRelationEntity();
            attrAttr.setAttrGroupId(attr.getAttrGroupId());
            attrAttr.setAttrId(attrEntity.getAttrId());
            attrAttrgroupRelationDao.insert(attrAttr);
        }

    }

    @Override
    public PageUtils queryBsesPage(Map<String, Object> params, Long catelogId, String type) {
        QueryWrapper<AttrEntity> Wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type", "base".equalsIgnoreCase(type) ? ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode() : ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());

        if (catelogId != 0) {

            Wrapper.eq("catelog_id", catelogId);
        }
        String key = (String) params.get("key");

        if (StringUtils.isNotEmpty(key)) {
            Wrapper.and((wrapper) -> {
                wrapper.eq("attr_id", key).or().like("attr_name", key);
            });

        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                Wrapper
        );
        PageUtils pageUtils = new PageUtils(page);

        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> attrlist = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            // 查询CatelogName
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            CategoryEntity categoryEntity = categoryDao.selectById(attrRespVo.getCatelogId());
            attrRespVo.setCatelogName(categoryEntity.getName());
            //设置分类和分组名
            AttrAttrgroupRelationEntity attrid = attrAttrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            if ("base".equalsIgnoreCase(type) && attrid != null) {
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrid.getAttrGroupId());
                attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrlist);

        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity byId = this.getById(attrId);
        BeanUtils.copyProperties(byId, attrRespVo);

        if (byId.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {

            // 关联表查询分组id
            AttrAttrgroupRelationEntity relation = attrAttrgroupRelationDao.selectOne(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrRespVo.getAttrId()));
            if (relation != null) {
                Long attrGroupId = relation.getAttrGroupId();
                attrRespVo.setAttrGroupId(attrGroupId);
                // 查询分组信息
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                if (attrGroupEntity != null) {
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }

            }
        }


        // 查询分类信息
        Long catelogId = attrRespVo.getCatelogId();
        Long[] ccatelogPath = categoryService.findCcatelogPath(catelogId);
        attrRespVo.setCatelogPath(ccatelogPath);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if (categoryEntity != null) {
            attrRespVo.setCatelogName(categoryEntity.getName());

        }

        return attrRespVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrRespVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        this.updateById(attrEntity);

        if (attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) {
            // 跟新关联表
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();

            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            // 判断是跟新还新增
            Integer count = attrAttrgroupRelationDao.selectCount(
                    new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity,
                        new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));

            } else {
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);

            }

        }


    }

    @Override
    public List<AttrEntity> getTolistAttr(Long attrgroupId) {

        List<AttrAttrgroupRelationEntity> attrgroupRelation = attrAttrgroupRelationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> collect = attrgroupRelation.stream().map((relationEntity) -> {

            return relationEntity.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> attrEntities = null;

        if (collect.size() > 0) {
            attrEntities = this.listByIds(collect);

        }
        return attrEntities;
    }

    @Override
    public PageUtils selectNolist(Long attrgroupId, Map<String, Object> params) {
        // 获取分组住的分类id
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 获取分类所有分组
        List<AttrGroupEntity> attrGroupList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> collect = attrGroupList.stream().map((entity) -> {

            return entity.getAttrGroupId();
        }).collect(Collectors.toList());

        // 获取所有分组已绑定属性

        List<AttrAttrgroupRelationEntity> attrgroupRelation = attrAttrgroupRelationDao.selectBatchIds(collect);
        List<Long> list = attrgroupRelation.stream().map((entity) -> {

            return entity.getAttrId();
        }).collect(Collectors.toList());

        // 获取当前分组可以绑定属性
        QueryWrapper<AttrEntity> attrQueryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId);

        if (list.size() > 0) {
            attrQueryWrapper.notIn("attr_id", list);
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                attrQueryWrapper
        );

        PageUtils utils = new PageUtils(page);

        return utils;
    }

    @Override
    public List<Long> selectSearAttrs(List<Long> collect) {

        return this.baseMapper.selectListById(collect);
    }

}