package com.example.demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.pojo.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Student)表数据库访问层
 *
 * @author makejava
 * @since 2023-07-18 11:09:47
 */
@Mapper
public interface StudentDao extends BaseMapper<Student> {

}

