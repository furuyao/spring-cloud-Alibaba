package com.guli.gulike.cart.interceptor;

import com.guli.common.constant.AuthServerConstant;
import com.guli.common.constant.CartConstant;
import com.guli.common.vo.MemberRerspVo;
import com.guli.gulike.cart.to.UserInfoTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @创建人: fry
 * @用于： 这个拦截器想要起作用还要配置需要拦截的请求
 * @创建时间 2020/9/6
 */
@Component
public class CarInterceptor implements HandlerInterceptor {

    /**
     * 该线程可以分享同一线程重开始到结束的东西
     */
    public static ThreadLocal<UserInfoTo> local = new ThreadLocal<>();

    /**
     * @Author: fry
     * @Description: 执行请求之前先判断是否登录在确定用那个购物车
     * @Param: [request, response, handler]
     * @Date: 2020/9/6 16:47
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        UserInfoTo userInfoTo = new UserInfoTo();

        HttpSession session = request.getSession();

        MemberRerspVo attribute = (MemberRerspVo) session.getAttribute(AuthServerConstant.LOGIN_USER);


        if (attribute != null) {
            // 如果登录了就放入用户id
            userInfoTo.setUserId(attribute.getId());

        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies.length>0){
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                        // 放入cookie值
                        userInfoTo.setUserKey(cookie.getValue());
                        userInfoTo.setTempUser(true);
                    }

                }
            }


        }

        /**
         * 判断此用户是否有临时购物车，没有就加一个
         */
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }



        //把用户是否登录形象放入
        local.set(userInfoTo);
        // return true 表示放行
        return true;
    }

    /**
    *@Author: fry
    *@Description: 业务执行后的拦截器
    *@Param:
    *@Date: 2020/9/6 17:17
    */

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = local.get();
        // 判断是否是临时用户
        if (!userInfoTo.isTempUser()){
            // 拿到userKey 让浏览器保存一个月，这个key是临时购物车的Key
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("gulimall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }

    }
}
