package com.sddzinfo.html2docx.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.lang.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Base64;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 图片工具
 * @date 2022-04-07
 **/
@Slf4j
public class ImageUtils {

    private static final BigDecimal MAX_WORD_IMAGE_WIGHT = BigDecimal.valueOf(600);
    private static final BigDecimal MAX_WORD_IMAGE_HEIGHT = BigDecimal.valueOf(800);

    private ImageUtils() {

    }

    /**
     * 下载图片,并转换成base64
     * 将过大的图片，调整到合适大小，参数可以通过上面两个值调整
     *
     * @param content 导出文本
     * @return 调整后的文本
     */
    public static String processImage(String content, String url_origin) {
        //转换文档。
        Document parse = Jsoup.parse(content, url_origin);
        Elements elements = parse.select("img");
        elements.forEach(
                ele -> {
                    String url = ele.attr("abs:src");
                    BufferedImage sourceImg = null;
                    InputStream image = getImage(url, url_origin);
                    if (image == null) {
                        // 图片不存在 不处理
                        return;
                    }
                    try {
                        sourceImg = ImageIO.read(image);
                    } catch (IOException e) {
                        log.error("读取图片异常", e);
                    }
                    if (sourceImg != null) {
                        BigDecimal width = BigDecimal.valueOf(sourceImg.getWidth());
                        BigDecimal height = BigDecimal.valueOf(sourceImg.getHeight());
                        if (width.compareTo(MAX_WORD_IMAGE_WIGHT) > 0) {
                            height = height.divide(width.divide(MAX_WORD_IMAGE_WIGHT, 1, BigDecimal.ROUND_CEILING), BigDecimal.ROUND_CEILING);
                            width = MAX_WORD_IMAGE_WIGHT;
                            //style="width: 610px; height: 407px;" width="549" height="304"
                            setAttr(ele, width, height);
                        }
                        if (height.compareTo(MAX_WORD_IMAGE_HEIGHT) > 0) {
                            width = width.divide(height.divide(MAX_WORD_IMAGE_HEIGHT, 1, BigDecimal.ROUND_CEILING), BigDecimal.ROUND_CEILING);
                            height = MAX_WORD_IMAGE_HEIGHT;
                            setAttr(ele, width, height);
                        }
                        //转为hbase64
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        try {
                            ImageIO.write(sourceImg, "png", os);
                        } catch (IOException e) {
                            log.error("写入图片异常", e);
                        }
                        byte[] bytes = os.toByteArray();
//                        BASE64Encoder encoder = new BASE64Encoder();
                        String encode = Base64.getEncoder().encodeToString(bytes);
//                        String encode = encoder.encode(bytes);
                        ele.attr("src", "data:image/png;base64," + encode);

                        //此步是为标签增加'</img>'结尾符号，防止报错
                        ele.appendText("");
                    }

                }
        );
        //去掉"html"字符串，方便后面拼接
        return parse.select("body").outerHtml();
    }


    /**
     * 获取图片,出现问题返回空
     *
     * @param url_pic 图片url
     * @return 图片流
     * @throws IOException 获取异常
     */
    @Nullable
    public static InputStream getImage(String url_pic) {
        return getImage(url_pic, url_pic);
    }

    /**
     * 获取图片,出现问题返回空
     *
     * @param url_pic     图片url
     * @param referer_url referer_url
     * @return 图片流
     * @throws IOException 获取异常
     */
    @Nullable
    public static InputStream getImage(String url_pic, String referer_url) {
        if (isBlank(url_pic)) {
            return null;
        }
        if (isBlank(referer_url)) {
            referer_url = url_pic;
        }
        try {
            return Request.Get(url_pic)
                    .addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36")
                    .addHeader(HttpHeaders.REFERER, referer_url)
                    .execute().returnContent().asStream();
        } catch (IOException e) {
            log.error("获取图片异常", e);
        }
        return null;
    }

    private static void setAttr(Element element, BigDecimal width, BigDecimal height) {
        element.attr("style", String.format("width: %spx; height: %spx;", width, height));
        element.attr("height", String.valueOf(height));
        element.attr("width", String.valueOf(width));
    }
}
