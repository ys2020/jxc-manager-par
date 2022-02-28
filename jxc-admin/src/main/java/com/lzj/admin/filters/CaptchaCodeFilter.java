package com.lzj.admin.filters;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lzj.admin.model.CaptchaImageModel;
import com.lzj.admin.model.RespBean;
import com.lzj.admin.utils.StringUtil;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
@Component
public class CaptchaCodeFilter extends OncePerRequestFilter {

    private static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (StringUtils.equals("/login", httpServletRequest.getRequestURI()) && StringUtils.equalsIgnoreCase(httpServletRequest.getMethod(),"post")){

            try {
                this.vaildate(new ServletWebRequest(httpServletRequest));
            } catch (AuthenticationException e) {
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                httpServletResponse.getWriter().write(objectMapper.writeValueAsString(
                        RespBean.error("验证码错误")));
                return;
            }
        }

    filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void vaildate(ServletWebRequest servletWebRequest) {

        HttpSession session = servletWebRequest.getRequest().getSession();

        String codeInrequset = servletWebRequest.getParameter("captchaCode");
        if (StringUtils.isEmpty(codeInrequset)){
            throw new SessionAuthenticationException("验证码不能为空");
        }

        CaptchaImageModel codeInsession = (CaptchaImageModel) session.getAttribute("captcha_key");
        if(ObjectUtils.isNull(codeInsession)){
            throw new SessionAuthenticationException("验证码不存在");
        }
        if (codeInsession.isExpired()){
            throw new SessionAuthenticationException("验证码已过期");
        }
        if(!StringUtils.equals(codeInsession.getCode(),codeInrequset)){
            throw new SessionAuthenticationException("验证码不正确");
        }
    }
}
