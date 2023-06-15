package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.juntai.tinder.utils.FileUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;

/**
 * <p>
 * 试验类型
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_experiment")
public class Experiment implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId
    private String id;

    /**
     * 试验名称
     */
    private String name;

    /**
     * 想定名
     */
    private String scenarioCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;

    /**
     * 持续时间，单位秒
     */
    private Long duration;

    /**
     * 红方情报
     */
    private String intelligenceRed;

    /**
     * 蓝方情报
     */
    private String intelligenceBlue;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime modifyTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 试验类型
     */
    private Integer type;

    private String photo;

    @TableField(exist = false)
    private Long forcesCount;

    public boolean valid() throws InvalidParameterException {
        if (StringUtils.isBlank(name)) {
            throw new InvalidParameterException("试验名称不能为空！");
        }
        if (StringUtils.isBlank(scenarioCode)) {
            throw new InvalidParameterException("想定编号不能为空！");
        }
        if (!FileUtil.isAlphaNumeric(scenarioCode)) {
            throw new InvalidParameterException("想定编号必须由数字和字母组成！");
        }
//        if (StringUtils.isBlank(description)) {
//            throw new InvalidParameterException("描述不能为空！");
//        }
        if (duration == null) {
            throw new InvalidParameterException("持续时间不能为空！");
        }
        return true;
    }
}
