package com.example.demo.util;

import com.example.demo.pojo.entity.Student;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

/**
 * pdf公共工具类
 *
 * @author tz
 **/
public class PdfUtils {
    public static void exportPdf(HttpServletResponse response, List<Student> list) throws Exception {
        response.setContentType("application/octet-stream; charset=UTF-8");
        response.setHeader("Content-Disposition", "inline; filename=\"example.pdf\"");

        Document document = generateItextPdfDocument(response.getOutputStream(), list);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        // 将PDF内容生成为字节数组
        byte[] pdfBytes = baos.toByteArray();

        // 设置响应内容的长度
        response.setContentLength(pdfBytes.length);

        // 将PDF内容写入响应的输出流中
        response.getOutputStream().write(pdfBytes);

        // 刷新输出流
        response.getOutputStream().flush();
    }

    private static Document generateItextPdfDocument(OutputStream os, List<Student> list) throws Exception {
        // document
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, os);

        // open
        document.open();

        // add content - pdf meta information
        document.addAuthor("pdai");
        document.addCreationDate();
        document.addTitle("pdai-pdf-itextpdf");
        document.addKeywords("pdf-pdai-keyword");
        document.addCreator("pdai");

        // add content -  page content

        // Title
        document.add(createTitle("这是测试导出pdf"));

        // Chapter 1
        document.add(createChapterH1("1. 知识准备"));
        document.add(createChapterH2("1.1 什么是POI"));
        document.add(createParagraph("Apache POI 是创建和维护操作各种符合Office Open XML（OOXML）标准和微软的OLE 2复合文档格式（OLE2）的Java API。用它可以使用Java读取和创建,修改MS Excel文件.而且,还可以使用Java读取和创建MS Word和MSPowerPoint文件。更多请参考[官方文档](https://poi.apache.org/index.html)"));
        document.add(createChapterH2("1.2 POI中基础概念"));
        document.add(createParagraph("生成xls和xlsx有什么区别？POI对Excel中的对象的封装对应关系？"));

        // Chapter 2
        document.add(createChapterH1("2. 实现案例"));
        document.add(createChapterH2("2.1 用户列表示例"));
        document.add(createParagraph("以导出用户列表为例"));

        // 表格
        PdfPTable table = new PdfPTable(new float[]{20, 40, 50});
        table.setTotalWidth(500);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.getDefaultCell().setBorder(1);

        // 设置表头
        Class<?> clazz = Student.class;
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            table.addCell(createCell(field.getName()));
        }
        // 设置单元格值
        for (int i = 0; i < list.size(); i++) {
            table.addCell(createCell(list.get(i).getId() + ""));
            table.addCell(createCell(list.get(i).getName()));
            table.addCell(createCell(list.get(i).getAge() + ""));
        }
        document.add(table);

        document.add(createChapterH2("2.2 图片导出示例"));
        document.add(createParagraph("以导出图片为例"));
        // 图片
        Resource resource = new ClassPathResource("test.jpg");
        Image image = Image.getInstance(resource.getURL());
        image.setAlignment(Element.ALIGN_CENTER);
        image.scalePercent(20); // 缩放
        document.add(image);

        // close
        document.close();
        return document;
    }

    /**
     * 创建title
     * @param content 内容
     */
    private static Paragraph createTitle(String content) throws IOException, DocumentException {
        Font font = new Font(getBaseFont(), 24, Font.BOLD);
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    /**
     * 创建H1标题
     * @param content 内容
     */
    private static Paragraph createChapterH1(String content) throws IOException, DocumentException {
        Font font = new Font(getBaseFont(), 22, Font.BOLD);
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        return paragraph;
    }
    /**
     * 创建H2标题
     * @param content 内容
     */
    private static Paragraph createChapterH2(String content) throws IOException, DocumentException {
        Font font = new Font(getBaseFont(), 18, Font.BOLD);
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        return paragraph;
    }

    private static Paragraph createParagraph(String content) throws IOException, DocumentException {
        Font font = new Font(getBaseFont(), 12, Font.NORMAL);
        Paragraph paragraph = new Paragraph(content, font);
        paragraph.setAlignment(Element.ALIGN_LEFT);
        paragraph.setIndentationLeft(12); //设置左缩进
        paragraph.setIndentationRight(12); //设置右缩进
        paragraph.setFirstLineIndent(24); //设置首行缩进
        paragraph.setLeading(20f); //行间距
        paragraph.setSpacingBefore(5f); //设置段落上空白
        paragraph.setSpacingAfter(10f); //设置段落下空白
        return paragraph;
    }
    /**
     * 创建表格单元格内容
     * @param content 内容
     */
    public static PdfPCell createCell(String content) throws IOException, DocumentException {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        Font font = new Font(getBaseFont(), 12, Font.NORMAL);
        cell.setPhrase(new Phrase(content, font));
        return cell;
    }

    private static BaseFont getBaseFont() throws IOException, DocumentException {
        return BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    }
}

