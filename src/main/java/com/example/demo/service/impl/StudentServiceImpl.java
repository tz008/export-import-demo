package com.example.demo.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.dao.StudentDao;
import com.example.demo.pojo.dto.ImportStudentDto;
import com.example.demo.pojo.entity.Student;
import com.example.demo.pojo.vo.ExportStudentVO;
import com.example.demo.service.StudentService;
import com.example.demo.util.ExcelUtils;
import com.example.demo.util.PdfUtils;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (Student)表服务实现类
 *
 * @author makejava
 * @since 2023-07-18 11:09:49
 */
@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, Student> implements StudentService {

    @Override
    public void exportTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.export("导入模板", null, ExportStudentVO.class, response);
    }

    @Override
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<Student> studentList = this.list();
        if (CollectionUtils.isEmpty(studentList)) {
            return;
        }
        List<ExportStudentVO> list = new ArrayList<>(studentList.size());
        studentList.forEach(student -> {
                    ExportStudentVO exportStudentVO = new ExportStudentVO();
                    BeanUtils.copyProperties(student, exportStudentVO);
                    list.add(exportStudentVO);
                }
        );
        ExcelUtils.export("学生信息", list, ExportStudentVO.class, response);

    }

    @Override
    public void exportPdf(HttpServletResponse response) throws IOException, DocumentException {
        List<Student> studentList = this.list();
        if (CollectionUtils.isEmpty(studentList)) {
            return;
        }
        PdfUtils.exportPdf(response,studentList.toString());
    }

    @Override
    public void importStudent(MultipartFile file) throws IOException {
        String ext = FileUtil.extName(file.getOriginalFilename());
        if(!"xlsx".equals(ext) && !"xls".equals(ext)){
            return;
        }
        List<?> data = ExcelUtils.syncReadModel(file, ImportStudentDto.class,0,1);
        if(CollectionUtils.isEmpty(data)){
            return;
        }
        List<Student> list = data.stream().map(item -> {
            Student student = new Student();
            BeanUtils.copyProperties(item,student);
            return student;
        }).collect(Collectors.toList());
        this.saveBatch(list);
    }
}

