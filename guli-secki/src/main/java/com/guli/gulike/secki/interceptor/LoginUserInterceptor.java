package com.guli.gulike.secki.interceptor;

import com.guli.common.constant.AuthServerConstant;
import com.guli.common.vo.MemberRerspVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @创建人: fry
 * @用于：
 * @创建时间 2020/9/8
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRerspVo>  local = new ThreadLocal<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 服务和服务之间调用不用登录 /order/status/{orderSn}

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        String requestURI = request.getRequestURI();
        boolean match = antPathMatcher.match("/currentSeckillSkus", requestURI);
        boolean machTo =  antPathMatcher.match("/sku/seckill/**", requestURI);



        if (match || machTo){

            return true;
        }

        HttpSession session = request.getSession();
        MemberRerspVo attribute = (MemberRerspVo)session.getAttribute(AuthServerConstant.LOGIN_USER);

        if (attribute != null) {

            local.set(attribute);
            return true;

        } else {
            session.setAttribute("msg","请先登录");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }


    }
}
