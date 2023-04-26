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
    FILE_EMPTY_ERROR(50001, "tinder.file_empty_error"),
    FILE_WRITE_ERROR(50002, "tinder.file_write_error"),
    FILE_CREATE_ERROR(50003, "tinder.file_create_error");



    private final int errorCode;
    private final String code;

}
