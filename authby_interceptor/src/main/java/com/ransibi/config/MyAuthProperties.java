package com.ransibi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: rsb
 * @description: 2023-11-16-11-14
 * @description:
 * @Version: 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "auth")
//默认是从application.properties/yml文件中读取，如果是需要从其它配置文件中读取的话，可以通过@PropertySource("classpath:/app.yml")注解进行配置
public class MyAuthProperties {
    /**
     * 请求头的key
     */
    private String flag = "headerFlag";
    /**
     * 接口鉴权方式: 0-不鉴权 ,1-摘要信息从header中获取 , 2-摘要信息从url中获取 ,3-摘要信息从body中获取;默认为0
     */
    private Integer way = 0;
    /**
     * 开放厂家，如果没配，那么就默认不鉴权，way为0
     */
    private String manufacturer;
    /**
     * 因为请求方和响应方可能会有时间偏差，所以需要允许一定的误差范围
     */
    private Integer maxinterval = 5;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Integer getWay() {
        return way;
    }

    public void setWay(Integer way) {
        this.way = way;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getMaxinterval() {
        return maxinterval;
    }

    public void setMaxinterval(Integer maxinterval) {
        this.maxinterval = maxinterval;
    }
}
