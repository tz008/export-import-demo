package com.example.demo.pojo.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * (Student)表实体类
 *
 * @author makejava
 * @since 2023-07-18 11:09:48
 */
@EqualsAndHashCode(callSuper = true)
@SuppressWarnings("serial")
@Data
public class Student extends Model<Student> {

    private Long id;

    private String name;

    private Integer age;

}

