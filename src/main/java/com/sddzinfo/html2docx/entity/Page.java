package com.sddzinfo.html2docx.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-11-01
 **/
@Data
public class Page<T> {
    private int pageNo;   //当前页
    /**
     * 总行数，未计算则赋值为-1
     */
    private int totalRows = -1;   //总行数
    int pageSize; //页大小

    private List<T> list = new ArrayList<>();

    /**
     * 总页数
     *
     * @return
     */
    public int getPages() {
        int pages;    //总页数
        if (totalRows % pageSize == 0) {
            pages = totalRows / pageSize;
        } else {
            pages = totalRows / pageSize + 1;
        }
        return pages;
    }
}
