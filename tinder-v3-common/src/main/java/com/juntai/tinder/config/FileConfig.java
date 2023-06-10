package com.juntai.tinder.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/23
 */
@Configuration
@Data
public class FileConfig {

    /**
     * 文件本地存储目录
     */
    @Value("${root.dir:/apps/files}")
    private String path;

}