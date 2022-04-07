package com.sddzinfo.html2docx.common.jpa.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageParameter implements java.io.Serializable {

    /**
     * 当前第几页
     */
    private int pageNum = 1;

    /**
     * 每页多少条数据
     */
    private int pageSize = 10;

    /**
     * 搜索，关键字
     */
    private String keywords;

}
