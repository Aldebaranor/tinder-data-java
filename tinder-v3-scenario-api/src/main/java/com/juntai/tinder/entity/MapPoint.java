package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 地图预置名称
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Getter
@Setter
@TableName("meta_map_point")
public class MapPoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private String id;

    /**
     * 试验编号
     */
    private String experimentId;

    /**
     * 名称
     */
    private String name;

    /**
     * 经度
     */
    private Object lon;

    /**
     * 纬度
     */
    private Object lat;

    /**
     * 海拔
     */
    private Object alt;

    /**
     * 航向角
     */
    private Object heading;

    /**
     * 横滚角
     */
    private Object roll;

    /**
     * 俯仰角
     */
    private Object pitch;

    /**
     * 上升角
     */
    private Object zoom;

    private Integer disabled;

    private Integer beDefault;

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
}
