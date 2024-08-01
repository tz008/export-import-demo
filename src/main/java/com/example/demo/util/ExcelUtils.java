package com.example.demo.util;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.http.HttpHeaders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;


/**
 * excel公共工具类
 *
 * @author tz
 * @date 2023/06/28 11:00
 **/
@Slf4j
@Component
public class ExcelUtils {

    private ExcelUtils() {
    }

    /**
     * 导出excel
     *
     * @param fileName 文件名称
     * @param data     数据
     * @param clazz    class
     * @param response resp
     */
    public static void export(String fileName, List<?> data, Class<?> clazz, HttpServletResponse response) throws IOException {
        EasyExcelFactory.write(response.getOutputStream(), clazz)
                .head(clazz)
                .registerWriteHandler(setCellStyle(fileName, response))
                .sheet(fileName)
                .doWrite(data);
    }

        /**
     * 导出带下拉框的excel
     *
     * @param fileName 文件名称
     * @param data     数据
     * @param clazz    class
     * @param response resp
     */
    public static void exportWithDropdown(String fileName, List<?> data, Class<?> clazz, HttpServletResponse response, List<String> dropDownList) throws IOException {
        EasyExcelFactory.write(response.getOutputStream(), clazz)
                .head(clazz)
                .registerWriteHandler(setCellStyle(fileName, response))
                .registerWriteHandler(new SheetWriteHandler() {
                    @Override
                    public void afterSheetCreate(WriteWorkbookHolder writeSheet, WriteSheetHolder writeSheetHolder) {
                        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
                        Sheet sheet = workbook.getSheetAt(0);
                        // 下拉框的选择区域
                        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 100, 1, 1);
                        // 创建下拉框
                        DataValidationHelper helper = sheet.getDataValidationHelper();
                        DataValidationConstraint constraint = helper.createExplicitListConstraint(dropDownList.toArray(new String[0]));
                        DataValidation dataValidation = helper.createValidation(constraint, cellRangeAddressList);
                        // 设置下拉框
                        sheet.addValidationData(dataValidation);
                    }
                })
                .sheet(fileName)
                .doWrite(data);
    }


    private static HorizontalCellStyleStrategy setCellStyle(String fileName, HttpServletResponse response) throws UnsupportedEncodingException {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 设置表头背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        // 设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short) 13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);

        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());

        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName + ".xlsx");
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param file       导入文件
     * @param clazz      模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<?> syncReadModel(MultipartFile file, Class<?> clazz, Integer sheetNo, Integer headRowNum) throws IOException {
        if (Objects.isNull(file)) {
            throw new RuntimeException("上传文件错误");
        }

        return EasyExcelFactory.read(file.getInputStream()).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }
}
