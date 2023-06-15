package com.juntai.tinder.exception;

import com.juntai.soulboot.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 错误码
 *
 * @author wangxuanbo
 * @date 2023-02-28
 */
@Getter
@RequiredArgsConstructor
public enum TinderErrorCode implements ErrorCode {


    /**
     * org_code不合法
     */
    TINDER_COMMON_ERROR(10000, "tinder.common_error"),
    FILE_ERROR(10001, "file_error"),

    ETCD_ERROR(10101, "etcd_error"),
    CACHE_ERROR(10102, "cache_error"),

    SCENARIO_MONITOR_ERROR(10201, "scenario_monitor_error"),
    SCENARIO_RUNTIME_ERROR(10202, "scenario_runtime_error"),
    TINDER_EQUIPMENT_ERROR(10301, "tinder_equipment_error"),
    TINDER_EQUIPMENT_TYPE_ERROR(10302, "tinder_equipmentType_error"),
    TINDER_TASK_TYPE_ERROR(10303, "tinder_taskType_error"),
    TINDER_FORCES_CARRY_ERROR(10304, "tinder_forcesCarry_error"),
    TINDER_FORCES_PLAN_ERROR(10305, "tinder_forcesPlan_error"),
    TINDER_GEOMETRY_ERROR(10306, "tinder_geometry_error"),
    TINDER_MAP_POINT_ERROR(10307, "tinder_mapPoint_error"),

    TINDER_FORCES_ERROR(10308, "tinder_forces_error"),

    TINDER_FORCES_LIBRARY_ERROR(10309, "tinder_forces_library_error"),
    TINDER_EXPERIMENT_ERROR(10310, "tinder_experiment_error"),


    ;


    private final int errorCode;
    private final String code;

}
