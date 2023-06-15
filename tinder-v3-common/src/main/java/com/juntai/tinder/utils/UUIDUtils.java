package com.juntai.tinder.utils;


import java.util.UUID;

/**
 * @Description:
 * @Author: nemo
 * @Date: 2022/5/25
 */
public final class UUIDUtils {
    private UUIDUtils() {
        throw new IllegalStateException("Utils");
    }


    public static long getLongUuid() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }


    public static String getHashUuid() {

        return String.valueOf(UUID.randomUUID().toString().hashCode() & Integer.MAX_VALUE);
    }

    public static String getHashUuid(String id) {

        return String.valueOf(id.hashCode() & Integer.MAX_VALUE);
    }


}
