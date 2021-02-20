package com.guli.guliproduclt.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.guli.guliproduclt.entity.AttrEntity;
import com.guli.guliproduclt.service.AttrAttrgroupRelationService;
import com.guli.guliproduclt.service.AttrService;
import com.guli.guliproduclt.service.CategoryService;
import com.guli.guliproduclt.vo.AttrGroupWithAttrsVo;
import com.guli.guliproduclt.vo.AttrgroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.guli.guliproduclt.entity.AttrGroupEntity;
import com.guli.guliproduclt.service.AttrGroupService;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.R;


/**
 * 属性分组
 *
 * @author fry
 * @email eeeeee@gmail.com
 * @date 2020-06-06 19:56:22
 */

@RestController
@RequestMapping("/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;



    ///product/attrgroup/{catelogId}/withattr

    @GetMapping("/{catelogId}/withattr")
    public R selectBycatelogIdList(@PathVariable("catelogId") Long catelogId){

      List<AttrGroupWithAttrsVo> vos = attrGroupService.selectBycatelogIdList(catelogId);

        return R.ok().put("data",vos);
    }




    /**
     * 查询关联关系
     */
    @RequestMapping("/{attrgroupId}/attr/relation")
    public R tolist(@PathVariable("attrgroupId") Long attrgroupId) {

        List<AttrEntity> attrEntities = attrService.getTolistAttr(attrgroupId);
        return R.ok().put("data", attrEntities);
    }

    /**
     * 查询该分组可以关联的属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R selectNolist(@PathVariable("attrgroupId") Long attrgroupId,
                          @RequestParam Map<String , Object> params){
       PageUtils utils =  attrService.selectNolist(attrgroupId, params);

        return R.ok().put("data",utils);
    }

    /**
     * 添加属性与分组关联关系
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrgroupRelationVo> vos ){

        attrAttrgroupRelationService.addRelation(vos);

        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{attrgrop}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("attrgrop") Long attrgrop) {
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils page = attrGroupService.queryPage(params, attrgrop);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R infoFather(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();

        Long[] longs = categoryService.findCcatelogPath(catelogId);
        attrGroup.setCatelogPath(longs);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/father/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        return R.ok().put("attrGroup", attrGroup);
    }


    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("guliproduclt:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("guliproduclt:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //  @RequiresPermissions("guliproduclt:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

    /**
     * 删除关联关系
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrgroupRelationVo[] RelationVos){

        attrAttrgroupRelationService.deleteRelation(RelationVos);
        return R.ok();
    }

}
