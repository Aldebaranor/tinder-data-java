package com.juntai.tinder.model;

import lombok.Data;

/**
 * @Description: 通用属性$
 * @Author: nemo
 * @Date: 2023/6/6 14:32
 */
@Data
public class PropertyItem<T> {

    private String name;
    private String text;
    private T value;

    public PropertyItem(String name, String text, T value) {
        this.name = name;
        this.text = text;
        this.value = value;
    }

    public PropertyItem() {
    }

    public static <V> PropertyItem<V> build() {
        PropertyItem<V> ncv = new PropertyItem();
        return ncv;
    }

    public static <V> PropertyItem<V> build(String name, String text, V value) {
        PropertyItem<V> ncv = new PropertyItem(name, text, value);
        return ncv;
    }
}
