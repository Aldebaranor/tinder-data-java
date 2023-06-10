package com.juntai.tinder.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author nemo
 * @date: 2022/05/20/5:24 下午
 * @description:
 */
@Data
public class ForcesLibraryUpdateModel implements Serializable {

    private static final long serialVersionUID = 4053411407624834430L;

    private String id;

    private String name;

    private String inputInfo;

    private Integer num;


}
