package com.ransibi.config;


import cn.hutool.crypto.digest.DigestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @description:
 * @author: rsb
 * @description: 2023-11-15-11-22
 * @description: 访问拦截器类，实现HandlerInterceptor接口，重写preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)方法，实现相关拦截规则
 * @Version: 1.0.0
 */
public class AccessInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String zxFlag = request.getHeader("zxflag");
        String flag = "xinneng";
        //获取当前时间的时间戳
        Date currentTime = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String formattedTime = sdf.format(currentTime);
        Date date = sdf.parse(formattedTime);
        long timestamp = date.getTime();
        System.out.println("timestamp：" + timestamp);
        //拼接厂家标记和当前时间戳
        flag += timestamp;
        System.out.println("加密前：" + flag);
        //通过sha256进行加密并转成16进制
        String sha256 = DigestUtil.sha256Hex(flag);
        System.out.println("加密后:" + sha256);
        if (StringUtils.isNotEmpty(zxFlag)) {
            if (zxFlag.equals(sha256)) {
                return true;
            } else {
                //不满足授权规则，重定向到认证失败接口，返回相应的异常信息
                response.sendRedirect(request.getContextPath() + "/login/toLoginPage");
            }
        }
        //默认拦截
        return false;
    }
}
