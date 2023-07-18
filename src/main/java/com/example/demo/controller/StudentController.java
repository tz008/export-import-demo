package com.example.demo.controller;

import com.example.demo.service.StudentService;
import com.itextpdf.text.DocumentException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author tz
 * @date 2023/07/18 11:10
 **/
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private StudentService studentService;

    @PostMapping("/exportTemplate")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        studentService.exportTemplate(response);
    }

    @PostMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        studentService.exportExcel(response);
    }

    @PostMapping("/exportPdf")
    public void exportPdf(HttpServletResponse response) throws IOException, DocumentException {
        studentService.exportPdf(response);
    }

    @PostMapping("/importStudent")
    public void importStudent(MultipartFile file) throws IOException {
        studentService.importStudent(file);
    }
}
