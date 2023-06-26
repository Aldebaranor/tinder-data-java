package com.juntai.tinder.condition;

import com.juntai.soulboot.data.annotation.Eq;
import com.juntai.soulboot.data.annotation.Gte;
import com.juntai.soulboot.data.annotation.Like;
import com.juntai.soulboot.data.annotation.Lte;
import com.juntai.tinder.entity.enums.UserType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 条件
 *
 * @author 奔波儿灞
 * @version 1.0.0
 */
@Data
public class UserCondition {


    @Like(column = "name")
    private String name;

    @Eq(column = "type")
    private UserType type;

    @Gte(column = "age")
    private Integer gteAge;

    @Like(column = "email")
    private String email;

    @Gte(column = "create_at")
    private LocalDateTime startCreateAt;

    @Lte(column = "create_at")
    private LocalDateTime endCreateAt;

}
