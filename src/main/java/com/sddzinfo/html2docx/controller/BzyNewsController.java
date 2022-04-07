package com.sddzinfo.html2docx.controller;

import com.sddzinfo.html2docx.common.jpa.controller.BaseController;
import com.sddzinfo.html2docx.entity.BzyNews;
import com.sddzinfo.html2docx.service.BzyNewsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2022-04-07
 **/
@Validated
@RestController
@RequestMapping("/bzyNews")
public class BzyNewsController extends BaseController<BzyNews, Integer, BzyNewsService> {
}
