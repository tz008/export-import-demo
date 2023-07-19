package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.pojo.entity.Student;
import com.itextpdf.text.DocumentException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * (Student)表服务接口
 *
 * @author makejava
 * @since 2023-07-18 11:09:49
 */
public interface StudentService extends IService<Student> {
    /**
     * 导出下载模板
     * @param response 返回流
     * @exception IOException io异常
     */
    void exportTemplate(HttpServletResponse response) throws IOException;
    /**
     * 导出excel
     * @param response 返回流
     * @exception IOException io异常
     */
    void exportExcel(HttpServletResponse response) throws IOException;

    /**
     * 导出pdf
     * @param response 返回流
     * @exception IOException io异常
     */
    void exportPdf(HttpServletResponse response) throws Exception;

    /**
     * 导入
     * @param file 文件
     */
    void importStudent(MultipartFile file) throws IOException;
}

