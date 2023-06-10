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
 * @since 2023-05-07
 */
@Getter
@Setter
@TableName("meta_model_relation")
public class ModelRelation implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 子模型id
     */
    private String memberId;

    /**
     * 所属模型id
     */
    private String belongId;
}
