package com.sddzinfo.html2docx.common.jpa.utils;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-12-16
 **/
public class Specifications {
    public static <T> SpecificationUtils<T> and() {
        return new SpecificationUtils<>(BooleanOperator.AND);
    }

    public static <T> SpecificationUtils<T> or() {
        return new SpecificationUtils<>(BooleanOperator.OR);
    }
}
