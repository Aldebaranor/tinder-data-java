package com.juntai.tinder.mapper;

import com.juntai.tinder.entity.ForcesPlan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 1敌我属性 0 红方，1，蓝方，2，白方 Mapper 接口
 * </p>
 *
 * @author nemo
 * @since 2023-06-07
 */
@Mapper
public interface ForcesPlanMapper extends BaseMapper<ForcesPlan> {

}
