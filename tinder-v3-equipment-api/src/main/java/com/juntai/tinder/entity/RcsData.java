package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@TableName("meta_rcs_data")
public class RcsData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 所属兵力id
     */
    private String groupId;

    /**
     * 类型
     */
    private String type;

    /**
     * 正面
     */
    private String rcs1;

    /**
     * 侧面
     */
    private String rcs2;

    /**
     * 后面
     */
    private String rcs3;

    /**
     * 上面
     */
    private String rcs4;
}
