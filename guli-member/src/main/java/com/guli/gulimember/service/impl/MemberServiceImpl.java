package com.guli.gulimember.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guli.common.utils.HttpUtils;
import com.guli.common.utils.R;
import com.guli.gulimember.entity.MemberLevelEntity;
import com.guli.gulimember.service.MemberLevelService;
import com.guli.gulimember.vo.MemberRegistVo;
import com.guli.gulimember.vo.MenberLoginVo;
import com.guli.gulimember.vo.SocialUserVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guli.common.utils.PageUtils;
import com.guli.common.utils.Query;

import com.guli.gulimember.dao.MemberDao;
import com.guli.gulimember.entity.MemberEntity;
import com.guli.gulimember.service.MemberService;

@Slf4j
@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {


    @Autowired
    MemberLevelService memberLevelService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public Map<String, Object> savRegist(MemberRegistVo vo) {

        Map<String, Object> map = new HashMap<>();
        MemberEntity memberEntity = new MemberEntity();
        MemberLevelEntity default_status = memberLevelService.getOne(new QueryWrapper<MemberLevelEntity>().eq("default_status", 1));

        Integer mobile = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", vo.getPhone()));

        if (mobile > 0) {
            map.put("code", 1);
            return map;
        }
        Integer username = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", vo.getUserName()));
        if (username > 0) {
            map.put("code", 2);
            return map;
        }
        // 存入会员名字
        memberEntity.setUsername(vo.getUserName());
        // 手机号
        memberEntity.setMobile(vo.getPhone());
        //默认会员
        memberEntity.setLevelId(default_status.getId());
        // 存储密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode(vo.getPassword());
        memberEntity.setPassword(encode);
        baseMapper.insert(memberEntity);
        map.put("code", 0);
        return map;
    }

    @Override
    public MemberEntity login(MenberLoginVo vo) {

        String loginacct = vo.getLoginacct();

        String password = vo.getPassword();

        MemberEntity entity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>()
                .eq("username", loginacct).or().eq("mobile", loginacct));
        if (entity == null) {

            //登录失败
            return null;
        } else {
            String passwordDb = entity.getPassword();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 密码比对
            boolean matches = passwordEncoder.matches(password, passwordDb);

            if (matches) {
                return entity;
            } else {
                return null;
            }

        }
    }

    @Override
    public MemberEntity oauth(SocialUserVo vo) {
        String uid = vo.getUid();
        // 确定是否注册过
        MemberEntity entity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        if (entity != null) {
            MemberEntity memberEntity = new MemberEntity();
            memberEntity.setId(entity.getId());
            String expires_in = vo.getExpires_in();
            Long aLong = Long.valueOf(expires_in);
            memberEntity.setExpiresIn(aLong);
            memberEntity.setAccessToken(vo.getAccess_token());
            this.baseMapper.updateById(memberEntity);
            entity.setAccessToken(vo.getAccess_token());
            entity.setExpiresIn(aLong);
            return entity;

        } else {
            MemberEntity member = new MemberEntity();


            try {
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("access_token", vo.getAccess_token());
                hashMap.put("uid", vo.getUid());
                // 没有注册过
                HttpResponse get = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<>(), hashMap);
                if (get.getStatusLine().getStatusCode() == 200) {

                    String key = EntityUtils.toString(get.getEntity());
                    JSONObject jsonObject = JSON.parseObject(key);
                    member.setUsername(jsonObject.getString("name"));
                    member.setGender("m".equals(jsonObject.getString("gender")) ? 1 : 0);
                    member.setNickname(jsonObject.getString("name"));
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                log.info(e.toString());
            }


            member.setSocialUid(vo.getUid());
            member.setAccessToken(vo.getAccess_token());
            Long aLong = Long.valueOf(vo.getExpires_in());
            member.setExpiresIn(aLong);
            this.baseMapper.insert(member);

            return member;
        }


    }

}