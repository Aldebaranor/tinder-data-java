package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 描述
 * </p>
 *
 * @author nemo
 * @since 2023-05-07
 */
@Data
@TableName("meta_model")
public class Model implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 装备id
     */
    private String equipmentId;

    /**
     * 版本号
     */
    private String version;

    /**
     * 模型库地址（对应网盘的文件夹id）
     */
    private String fileId;

    /**
     * 输入参数
     */
    private String inputInfo;

    /**
     * 输出参数
     */
    private String outputInfo;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    /**
     * 提供方
     */
    private String provider;

    /**
     * 状态
     */
    private String status;

    /**
     * 描述
     */
    private String description;
}
