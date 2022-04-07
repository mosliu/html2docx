package com.sddzinfo.html2docx.common.jpa.utils;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-12-16
 **/
public enum BooleanOperator {
    /**
     * and
     */
    AND("and"),

    /**
     * or
     */
    OR("or");

    private final String code;


    BooleanOperator(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
