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

    ETCD_ERROR(10101,"etcd_error"),
    CACHE_ERROR(10102,"cache_error"),

    SCENARIO_MONITOR_ERROR(10201,"scenario_monitor_error"),
    SCENARIO_RUNTIME_ERROR(10202,"scenario_runtime_error"),
    TINDER_EQUIPMENT_ERROR(10301,"tinder_equipment_error"),
    TINDER_EQUIPMENT_TYPE_ERROR(10302,"tinder_equipmentType_error"),
    TINDER_TASK_TYPE_ERROR(10303,"tinder_taskType_error"),


    ;



    private final int errorCode;
    private final String code;

}
