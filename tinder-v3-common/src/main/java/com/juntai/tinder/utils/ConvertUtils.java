package com.juntai.tinder.utils;

import com.juntai.soulboot.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: $
 * @Author: nemo
 * @Date: 2023/6/6 18:04
 */
public class ConvertUtils {

    public static <T> Map<String, T> convertMap(Map<Object, Object> kvs, Class<T> clz) {
        Map<String, T> map = new HashMap();
        assert kvs != null;

        kvs.forEach((k, v) -> {
            String kk = k != null ? k.toString() : "";
            if (!StringUtils.isBlank(kk)) {
                if (clz == String.class) {
                    map.put(kk, (T) v.toString());
                } else {
                    T t = JsonUtils.read(v.toString(), clz);
                    map.put(kk, t);
                }
            }

        });
        return map;
    }
}
