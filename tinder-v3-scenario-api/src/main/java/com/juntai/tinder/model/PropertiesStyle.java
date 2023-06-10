package com.juntai.tinder.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName Shape
 * @Description
 * @Author ShiZuan
 * @Date 2022/6/13 10:11
 * @Version
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertiesStyle implements Serializable {

    private static final long serialVersionUID = 49529291325794000L;
    private String fill;

    private String stroke;

    private String line;

    private int width;


}
