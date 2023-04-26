package com.juntai.tinder.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * <p>
 * 系统提示
 * </p>
 *
 * @author nemo
 * @since 2023-04-09
 */
@Data
@TableName("t_notice")
public class Notice implements Serializable {

    @TableId
    private String id;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;
}
