package com.oa5.dao;

import com.oa5.pojo.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface EmpDao {
    //分页查询员工数据
    @Select("select emp.*,dept_name,duty_name from " +
            "day.emp left join department on department.dept_id = emp.dept_id " +
            "left join duty on emp.duty_id = duty.duty_id order by number ")
    List<Emp> selectByPageHelper();

    //查询部门列表
    @Select("select * from day.department")
    List<Department> getDeptData();

    //查询职务列表
    @Select("select * from day.duty")
    List<Duty> getDutyData();


    //添加员工4——查找员工是否存在
    @Select("select * from day.emp where name=#{name}   ")
    Emp selectByName(Emp emp);

    //添加员工
    @Insert("insert into day.emp (name, birthday, address, dept_id, duty_id) VALUES (#{name} ,#{birthday} ,#{address} ,#{dept_id} ,#{duty_id}  )")
    int addEmp(Emp emp);

    //更新员工信息
    @Update("update day.emp set name=#{name} ,birthday=#{birthday} ,address=#{address} where number=#{number} ")
    int updateEmp(Emp emp);

    //删除员工考勤信息
    @Delete("delete from sign where number=#{number} ")
    int deleteEmpSignByNumber(Emp emp);

    //删除员工信息
    @Delete("delete from day.emp where number=#{number} ")
    int deleteEmp(Emp emp);

    //更新职务信息
    @Update("update day.emp set dept_id=#{dept_id} ,duty_id=#{duty_id}  where number=#{number} ")
    int updateDD(Emp emp);

    //统计员工人数
    @Select("select count(*) from day.emp")
    int countUser();

    //获取每个员工的编号
    @Select("select number from day.emp")
    List<Integer> selectAllEmpNumber();
}
