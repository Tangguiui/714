package com.oa5.dao;

import com.oa5.pojo.Admin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface AdmDao {

    //通过名字查询管理员信息
    @Select("select * from day.admin where name=#{name} ")
    Admin selectByName(Admin admin);

    //管理员注册
    @Insert("insert into day.admin (name, pwd) VALUES (#{name} ,#{pwd} )")
    int insertAdm(Admin admin);

}
