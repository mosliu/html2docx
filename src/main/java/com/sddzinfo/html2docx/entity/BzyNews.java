package com.sddzinfo.html2docx.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 八爪鱼 临时抓取
 */
@Data
@Entity
@Table(name = "bzy_news")
public class BzyNews implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "author")
    private String author;

    @Column(name = "tag")
    private String tag;

    @Column(name = "tag1")
    private String tag1;

    @Column(name = "add_time")
    private String addTime;

    @Column(name = "content")
    private String content;

    @Column(name = "media_name")
    private String mediaName;

    @Column(name = "viewnum")
    private String viewnum;

    @Column(name = "st")
    private Integer st;

}
