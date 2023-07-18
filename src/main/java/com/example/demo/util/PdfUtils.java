package com.example.demo.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * pdf公共工具类
 *
 * @author tz
 **/
public class PdfUtils {
    public static void exportPdf(HttpServletResponse response,String content) throws IOException, DocumentException {
//        response.setContentType("application/pdf");
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "inline; filename=\"example.pdf\"");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();

        document.add(new Paragraph(content));

        document.close();

        // 将PDF内容生成为字节数组
        byte[] pdfBytes = baos.toByteArray();

        // 设置响应内容的长度
        response.setContentLength(pdfBytes.length);

        // 将PDF内容写入响应的输出流中
        response.getOutputStream().write(pdfBytes);

        // 刷新输出流
        response.getOutputStream().flush();
    }
}

