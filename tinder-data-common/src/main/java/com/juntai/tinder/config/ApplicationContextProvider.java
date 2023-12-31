package com.juntai.tinder.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: 容器上下文$
 * @Author: nemo
 * @Date: 2023/6/6 15:32
 */

@Component
public class ApplicationContextProvider implements ApplicationContextAware {
    private static ApplicationContext applicationContextSpring;

    /**
     * 通过class 获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return applicationContextSpring.getBean(clazz);
    }

    @Override
    public synchronized void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        applicationContextSpring = applicationContext;
    }

}
