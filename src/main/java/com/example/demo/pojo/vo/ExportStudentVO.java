package com.example.demo.pojo.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

/**
 * @author tz
 * @date 2023/07/18 11:15
 **/
@Data
public class ExportStudentVO {
    @ExcelIgnore
    private Long id;
    @ExcelProperty("姓名")
    @ColumnWidth(15)
    private String name;
    @ExcelProperty("年龄")
    @ColumnWidth(15)
    private Integer age;
}
