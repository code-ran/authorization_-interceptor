package com.ransibi.config;


import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @description:
 * @author: rsb
 * @description: 2023-11-15-11-22
 * @description: 访问拦截器类，实现HandlerInterceptor接口，重写preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)方法，实现相关拦截规则
 * @Version: 1.0.0
 */
@Component
public class AccessInterceptor implements HandlerInterceptor {
    //注入授权配置对象
    @Autowired
    private MyAuthProperties myAuthProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String manufacturer = myAuthProperties.getCompany();
        //默认不鉴权
        if (myAuthProperties.getWay() == 0) {
            return true;
        }
        if (StringUtils.isEmpty(manufacturer)) {
            //开放机构没配，默认不鉴权
            return true;
        }
        //授权规则为，以当前时间为准，前后不相差所配置最大时间间隔max_num
        int max_num = myAuthProperties.getMaxinterval();
        System.out.println("鉴权误差范围(分钟):" + max_num);
        //使用请求头的摘要信息鉴权
        if (myAuthProperties.getWay() == 1) {
            //获取请求头中的摘要信息
            String abstractInfo = request.getHeader(myAuthProperties.getFlag());
            System.out.println("请求header中获取的摘要信息:{}" + abstractInfo);
            if (StringUtils.isNotEmpty(abstractInfo)) {
                if (authRule(max_num, manufacturer, abstractInfo)) {
                    return true;
                }
            }
            //不满足授权规则，重定向到认证失败接口，返回相应的异常信息
            response.sendRedirect(request.getContextPath() + "/user/error");
        }
        //使用url中摘要参数鉴权
        if (myAuthProperties.getWay() == 2) {
            //获取url中的摘要信息
            String abstractInfo = request.getParameter(myAuthProperties.getFlag());
            System.out.println("请求url中获取的摘要信息:" + abstractInfo);
            if (StringUtils.isNotEmpty(abstractInfo)) {
                if (authRule(max_num, manufacturer, abstractInfo)) {
                    return true;
                }
            }
            //不满足授权规则，重定向到认证失败接口，返回相应的异常信息
            response.sendRedirect(request.getContextPath() + "/user/error");
        }
        //使用body中摘要参数授权
        if (myAuthProperties.getWay() == 3) {
            ServletInputStream inputStream = request.getInputStream();
            byte[] bytes = new byte[request.getContentLength()];
            inputStream.read(bytes);
            String s = new String(bytes, "utf-8");
            JSONObject jsonObject = JSON.parseObject(s);
            String abstractInfo = jsonObject.get(myAuthProperties.getFlag()).toString();
            System.out.println("post请求body中获取的摘要信息:" + abstractInfo);
            if (StringUtils.isNotEmpty(abstractInfo)) {
                if (authRule(max_num, manufacturer, abstractInfo)) {
                    return true;
                }
            }
            //不满足授权规则，重定向到认证失败接口，返回相应的异常信息
            response.sendRedirect(request.getContextPath() + "/user/error");
        }
        //默认拦截
        return false;
    }

    private static boolean authRule(int max_num, String manufacturer, String abstractInfo) {
        Date nowDate = new Date();
        //当前时间比对
        if (checkAbstractInfo(manufacturer, nowDate, abstractInfo)) {
            return true;
        }
        for (int i = 0; i < max_num; i++) {
            Date nextDate = DateUtil.offsetMinute(nowDate, i + 1);
            //向后伸缩比对
            if (checkAbstractInfo(manufacturer, nextDate, abstractInfo)) {
                return true;
            }
            //对于向前伸缩时间，需要排除当前时间，避免与向后伸缩时间重复
            Date lastDate = DateUtil.offsetMinute(nowDate, -i - 1);
            //向前伸缩比对
            if (checkAbstractInfo(manufacturer, lastDate, abstractInfo)) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkAbstractInfo(String manufacturer, Date date, String abstractInfo) {
        DateTime changeTime = DateUtil.date(date);
        System.out.println("伸缩时间后:" + changeTime.toString());
        String dateStr = changeTime.toString("yyyyMMddHHmm");
        System.out.println("伸缩时间后格式化后:" + dateStr);
        //拼接机构标记和当前时间组成摘要并加密
        String abstractInfoTmp = manufacturer + dateStr;
        System.out.println("加密前的摘要信息:" + abstractInfoTmp);
        String abstractBySha256 = DigestUtil.sha256Hex(abstractInfoTmp);
        System.out.println("加密后的摘要信息:" + abstractBySha256);
        return abstractBySha256.equals(abstractInfo);
    }
}
