package com.sddzinfo.html2docx.common.dto;

import com.google.gson.annotations.Expose;
import com.sddzinfo.html2docx.common.constants.RtnDtoCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 结果DTO
 * @date 2020-10-28
 **/
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class CommonResponseDto {
    /**
     * 状态码
     */
    @Expose
    int code;
    /**
     * 信息实体
     */
    @Expose
    Object data;
    /**
     * 状态说明
     */
    @Expose
    String msg;
    /**
     * 时间戳
     */
    @Expose
    Long timestamp;

    public CommonResponseDto() {
        this(0);
    }

    public CommonResponseDto(int code) {
        this(code, null);
    }

    public CommonResponseDto(RtnDtoCode code) {
        this(code, null);
    }

    public CommonResponseDto(int code, Object data) {
        this(code, "", data);
    }

    public CommonResponseDto(RtnDtoCode code, Object data) {
        this(code.code(), code.msg(), data);
    }

    public CommonResponseDto(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        timestamp = System.currentTimeMillis();
    }

    public static CommonResponseDto success() {
        CommonResponseDto dto = new CommonResponseDto(RtnDtoCode.SUCCESS);
        return dto;
    }

    public static CommonResponseDto success(Object data) {
        CommonResponseDto dto = new CommonResponseDto(RtnDtoCode.SUCCESS);
        if (data != null) {
            dto.data = data;
        }
        return dto;
    }

    public static CommonResponseDto success(String msg, Object data) {
        CommonResponseDto dto = new CommonResponseDto(RtnDtoCode.SUCCESS);
        if (msg != null) {
            dto.setMsg(msg);
        }
        if (data != null) {
            dto.data = data;
        }
        return dto;
    }

    public static CommonResponseDto fail(RtnDtoCode code) {
        CommonResponseDto dto = new CommonResponseDto(code);
        return dto;
    }

    /**
     * 异常失败
     *
     * @param data
     * @return
     */
    public static CommonResponseDto fail(Object data) {
        return fail(RtnDtoCode.EXCEPTIONS, data);
    }

    public static CommonResponseDto failCommon(Object data) {
        return fail(RtnDtoCode.FailCommon, data);
    }

    public static CommonResponseDto fail401(Object data) {
        return fail(RtnDtoCode.Fail401, data);
    }

    public static CommonResponseDto fail403(Object data) {
        return fail(RtnDtoCode.Fail403, data);
    }

    public static CommonResponseDto fail(RtnDtoCode code, Object data) {
        CommonResponseDto dto = new CommonResponseDto(code, data);
        return dto;
    }
}
