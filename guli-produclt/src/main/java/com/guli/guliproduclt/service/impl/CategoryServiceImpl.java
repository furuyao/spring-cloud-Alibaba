package com.guli.guliproduclt.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.guli.guliproduclt.service.CategoryBrandRelationService;
import com.guli.guliproduclt.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.guliproduclt.dao.CategoryDao;
import com.guli.guliproduclt.entity.CategoryEntity;
import com.guli.guliproduclt.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWhiBel() {
        //获取所有数据
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //获取一级分类
        List<CategoryEntity> collect = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((num) -> {
            num.setChildren(children(num, entities));
            return num;
        }).sorted((mebe1, mebe2) -> {
            return (mebe1.getSort() == null ? 0 : mebe1.getSort()) - (mebe2.getSort() == null ? 0 : mebe2.getSort());
        }).collect(Collectors.toList());

        return collect;
    }

    //查找子菜单
    private List<CategoryEntity> children(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> collect = all.stream().filter((categoryE) -> {
            return root.getCatId().equals(categoryE.getParentCid());
        }).map(categoryEntity -> {
                    categoryEntity.setChildren(children(categoryEntity, all));
                    return categoryEntity;
                }
        ).sorted((menb1, menb2) -> {
            return (menb1.getSort() == null ? 0 : menb1.getSort()) - (menb2.getSort() == null ? 0 : menb2.getSort());
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void rremoveMenuById(List<Long> asList) {
        //TODO 判断是否被应用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCcatelogPath(Long catelogId) {

        List<Long> longs = new ArrayList<>();

        List<Long> catelog = getCatelog(catelogId, longs);
        Collections.reverse(catelog);
        return longs.toArray(new Long[catelog.size()]);
    }
//    @CacheEvict(value = "category", key = "'selectCategorys'")  // 给缓存开启失效模式 记得''号


    //    @Caching(evict = {
//            @CacheEvict(value = "category", key = "'selectCategorys'"),
//            @CacheEvict(value = "category", key = "'getCatalogJsontoList'")
//    })
    @CacheEvict(value = "category", allEntries = true)// 删除这个缓存组下的所有
    @Transactional
    @Override
    public void updateClassification(CategoryEntity category) {
        this.updateById(category);
        if (!StringUtils.isEmpty(category.getName())) {
            categoryBrandRelationService.updateClassification(category.getName(), category.getCatId());
            // TODO 跟新冗余
        }
    }

    @Cacheable(value = {"category"}, key = "#root.method.name")   // 此注解用于开启缓存, 如果缓存中有就重缓存中获取,没有就吧此方法的结果放进缓存
    @Override
    public List<CategoryEntity> selectCategorys() {

        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));

        return categoryEntities;
    }

    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {

        String catalogJOSN = (String) redisTemplate.opsForValue().get("catalogJOSN");
        // 判断缓存中是否有数据 没有就进入下一步上锁查DB
        if (StringUtils.isEmpty(catalogJOSN)) {
            Map<String, List<Catelog2Vo>> catalogJsonto = getCatalogJsonto();

            return catalogJsonto;
        }
        Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJOSN, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return stringListMap;

    }


    public Map<String, List<Catelog2Vo>> getCatalogJsonto() {

        String catalogJOSN = (String) redisTemplate.opsForValue().get("catalogJOSN");
        if (!StringUtils.isEmpty(catalogJOSN)) {
            Map<String, List<Catelog2Vo>> stringListMap = JSON.parseObject(catalogJOSN, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });

            return stringListMap;
        }

        // 用redisson加分布式锁
        RLock getCatalog = redisson.getLock("getCatalog_json");
        getCatalog.lock();

        Map<String, List<Catelog2Vo>> catalogJsontoList;

        try {

            catalogJsontoList = getCatalogJsontoList();
        } finally {
            getCatalog.unlock();

        }
        return catalogJsontoList;


    }

    @Cacheable(value = {"category"}, key = "#root.method.name")
    public Map<String, List<Catelog2Vo>> getCatalogJsontoList() {


        List<CategoryEntity> categoryId = baseMapper.selectList(null);

        List<CategoryEntity> categoryEntities = getParent_cid(categoryId, 0L);

        Map<String, List<Catelog2Vo>> collect1 = categoryEntities.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            List<CategoryEntity> category = getParent_cid(categoryId, v.getCatId());
            List<Catelog2Vo> collect = null;
            if (category != null) {
                collect = category.stream().map(tem -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, tem.getName(), tem.getCatId().toString());
                    // 封装二级下的三级菜单    先查出二级下的三级
                    List<CategoryEntity> entities = getParent_cid(categoryId, tem.getCatId());
                    if (entities != null) {
                        List<Catelog2Vo.Catelog3Vo> catelog3Vos = entities.stream().map(list -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(tem.getCatId().toString(), list.getCatId().toString(), list.getName());

                            return catelog3Vo;
                        }).collect(Collectors.toList());

                        catelog2Vo.setCatalog3List(catelog3Vos);
                    }

                    return catelog2Vo;
                }).collect(Collectors.toList());

            }
            return collect;
        }));
//        String key = JSON.toJSONString(collect1);       // 因为用了 @Cacheable(value = {"category"}, key = "#root.method.name")注解所以不需要手动放入缓存
//        redisTemplate.opsForValue().set("catalogJOSN", key, 1, TimeUnit.DAYS);
        return collect1;
    }


    private List<CategoryEntity> getParent_cid(List<CategoryEntity> categoryId, Long v) {
        List<CategoryEntity> collect = categoryId.stream().filter(itm -> itm.getParentCid() == v).collect(Collectors.toList());

        return collect;
    }

    /**
     * 递归查询父节点
     */
    private List<Long> getCatelog(Long catelogId, List<Long> longs) {
        longs.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        try {

            if (byId.getCatId() != null) {
                getCatelog(byId.getParentCid(), longs);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return longs;
    }


}