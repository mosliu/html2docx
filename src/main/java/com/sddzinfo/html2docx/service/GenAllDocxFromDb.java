package com.sddzinfo.html2docx.service;

import com.sddzinfo.html2docx.entity.BzyNews;
import com.sddzinfo.html2docx.utils.Html2DocxIceBlueConverter;
import com.sddzinfo.html2docx.utils.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2022-04-07
 **/
@Component
@Slf4j
public class GenAllDocxFromDb {
    @Autowired
    BzyNewsService bzyNewsService;

    @PostConstruct
    public void init() {
        log.info("init");
        startExport();
    }

    private void startExport() {
        //加载所有数据
        List<BzyNews> all = bzyNewsService.findAll();
//        ForkJoinPool forkJoinPool = new ForkJoinPool(6);
//        forkJoinPool.submit(() -> {
//            all.parallelStream().forEach((bzyNews) -> {
//                doBzyNewsToDocx(bzyNews);
//            });
//        });

        all.parallelStream().forEach((bzyNews) -> {
            String title = bzyNews.getTitle();
            Integer id = bzyNews.getId();
            String filename = "d:/out/" + id + '.' + title + ".docx";
            if (Files.exists(Paths.get(filename))) {
                log.info("{} 已存在，跳过", filename);
            } else {
                doBzyNewsToDocx(bzyNews);
            }
        });

//        all.forEach(bzyNews -> {
//            doBzyNewsToDocx(bzyNews);
//        });
    }

    private void doBzyNewsToDocx(BzyNews bzyNews) {
        String title = bzyNews.getTitle();
        Integer id = bzyNews.getId();
        if (isBlank(title)) {
            title = "" + System.currentTimeMillis();
        }

        String content_origin = bzyNews.getContent();
        if (isBlank(content_origin)) {
            log.info("content is empty,id:{}", bzyNews.getId());
            return;
        }
        // 添加标题
        String content = "<p style=\"text-align: center;\"><h1>" + title + "</h1></p> <br/>" + content_origin;
        String url = bzyNews.getUrl();
        //开始生成
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("加载图片");
        String s = ImageUtils.processImage(content, url);
        stopWatch.stop();
        stopWatch.start("转换文档");
        String context = HEAD + s + FOOT;

        //这里是必须要设置编码的，不然导出中文就会乱码。
        byte[] b = context.getBytes(StandardCharsets.UTF_8);
        //将字节数组包装到流中
        OutputStream outputStream = null;
        try {
            outputStream = Files.newOutputStream(Paths.get("d:/out/" + id + '.' + title + ".docx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Html2DocxIceBlueConverter blueConvert = new Html2DocxIceBlueConverter(context, outputStream);
        blueConvert.convert();
        stopWatch.stop();
        String s1 = stopWatch.prettyPrint();

        log.info("导出<<{}>>用时:{}", title, s1);
    }

    public static final String HEAD = "<html><head></head>";
//    public static final String WORD_HEAD = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" +
//            "<html xmlns=\"http://www.w3.org/TR/REC-html40\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" xmlns:m=\"http://schemas.microsoft.com/office/2004/12/omml\">" +
//            "<head><meta name=\"ProgId\" content=\"Word.Document\" /><meta name=\"Generator\" content=\"Microsoft Word 12\" />" +
//            "<meta name=\"Originator\" content=\"Microsoft Word 12\" /> " +
//            "<!--[if gte mso 9]><xml><w:WordDocument><w:View>Print</w:View></w:WordDocument></xml><[endif]-->" +
//            "</head>";


    public static final String FOOT = "</html>";

}
