package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_map_style")
public class MapStyle implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    private String kind;

    /**
     * 类型
     */
    private String type;

    /**
     * 阵营
     */
    private String team;

    /**
     * 样式
     */
    private String style;

    private String description;
}
