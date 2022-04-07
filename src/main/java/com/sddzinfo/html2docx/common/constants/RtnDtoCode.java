package com.sddzinfo.html2docx.common.constants;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 返回实体使用的Code
 * @date 2020-11-18
 **/

public enum RtnDtoCode {
    /**
     * 成功
     */
    SUCCESS(0, "成功"),

    BIGFAIL(-1, "重大失败"),
    EXCEPTIONS(-2, "异常"),

    INPUTMISSINGFIELD(-101, "用户输入不完整，缺失字段"),

    INPUTNODATA(-102, "未传入数据"),

    INPUTALLEMPTY(-100, "用户输入属性域全部为空"),
    Fail401(-401, "未认证"),
    FailCommon(-100, "失败！"),
    Fail403(-403, "禁止访问"),
    Fail404(-404, "页面失联")
    //
    ;

    // 成员变量
    int code;
    String msg;

    // 构造方法
    RtnDtoCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public String msg() {
        return msg;
    }

    //覆盖方法
    @Override
    public String toString() {
        return this.code + "_" + this.msg;
    }
}
