package com.sddzinfo.html2docx.utils;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.MarginsF;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.util.List;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2022-04-07
 **/
@Slf4j
public class Html2DocxIceBlueConverter {

    private final String context;

    private final OutputStream outputStream;

    public Html2DocxIceBlueConverter(String context, OutputStream outputStream) {
        this.context = context;
        this.outputStream = outputStream;
    }

    public void convert() {
        try {
            //新建Document对象
            Document document = new Document();
            //添加section
            Section sec = document.addSection();
            //设置页面边距
            MarginsF margins = sec.getPageSetup().getMargins();
            margins.setTop(50f);
            margins.setBottom(50f);
            //添加段落并写入HTML文本
            sec.addParagraph().appendHTML(context);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();


            //文档另存为docx
            document.saveToStream(buffer, FileFormat.Docx);

            //转为输入流
            byte[] bytes = buffer.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(bytes);
            postConvertRemoveSprireDoc(inputStream);

        } catch (Exception e) {
            log.error("导出失败", e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                log.error("err", e);
            }
        }
    }

    /**
     * 移除版权声明
     *
     * @param docFilePath
     */
    public static void postConvertRemove1stLine(String docFilePath) {
        try (FileInputStream in = new FileInputStream(docFilePath)) {
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(in));
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            if (paragraphs.size() < 1) return;
            XWPFParagraph firstParagraph = paragraphs.get(0);
            if (firstParagraph.getText().contains("Spire.Doc")) {
                doc.removeBodyElement(doc.getPosOfParagraph(firstParagraph));
            }
            OutputStream out = new FileOutputStream(docFilePath);
            doc.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除版权声明
     *
     * @param inputStream
     */
    private void postConvertRemoveSprireDoc(InputStream inputStream) {
        try {
            OPCPackage opcPackage = OPCPackage.open(inputStream);
            XWPFDocument doc = new XWPFDocument(opcPackage);
            List<XWPFParagraph> paragraphs = doc.getParagraphs();
            if (paragraphs.size() < 1) return;
            XWPFParagraph firstParagraph = paragraphs.get(0);
            if (firstParagraph.getText().contains("Spire.Doc")) {
                doc.removeBodyElement(doc.getPosOfParagraph(firstParagraph));
            }
            doc.write(this.outputStream);
        } catch (IOException | InvalidFormatException e) {
            log.error("移除第一行时出错", e);
        }
    }

}
