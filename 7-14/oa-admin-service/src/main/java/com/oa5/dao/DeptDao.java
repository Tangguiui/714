package com.oa5.dao;

import com.oa5.pojo.Department;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DeptDao {

    // 查询所有部门和人数
    @Select("select department.dept_id,department.dept_name,count(emp.dept_id) as dept_num " +
            "from day.department " +
            "left join day.emp on department.dept_id = emp.dept_id " +
            "group by department.dept_id order by department.dept_id")
    List<Department> selectAllDeptAndNum();

    // 统计部门数
    @Select("select count(*) from day.department")
    int countDept();

    // 更新部门信息
    @Update("update day.department set dept_name=#{dept_name} where dept_id=#{dept_id} ")
    int updateDeptNameById(Department department);

    // 通过部门名字查询部门
    @Select("select * from day.department where dept_name=#{dept_name} ")
    Department selectByName(Department department);

    // 添加新的部门
    @Insert("insert into day.department(dept_name) values (#{dept_name} )")
    int addDept(Department department);

    // 查询部门员工数
    @Select("select count(*) from day.emp where dept_id = #{dept_id}")
    int getDeptEmployeeCount(int dept_id);

    // 删除部门
    @Delete("delete from day.department where dept_id = #{dept_id}")
    int deleteDept(int dept_id);
}