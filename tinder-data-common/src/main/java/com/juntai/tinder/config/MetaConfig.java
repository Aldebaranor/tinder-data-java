package com.juntai.tinder.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/4/2
 */
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "meta")
public class MetaConfig {

    private String unpackServiceCode;

    private String simulationUrlHead;

    private String etcdKey;

    private String iotDbUrl;

    private String recordsUrl;


}
