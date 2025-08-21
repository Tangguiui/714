package com.oa5.dao;

import com.oa5.pojo.Duty;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface DutyDao {
    // 查询所有职务和人数
    @Select("select duty.duty_id,duty.duty_name,count(emp.duty_id) as duty_num " +
            "from day.duty " +
            "left join day.emp on duty.duty_id = emp.duty_id " +
            "group by duty.duty_id order by duty.duty_id ")
    List<Duty> selectAllDutyAndNum();

    // 统计职务数
    @Select("select count(*) from day.duty")
    int countDuty();

    // 更新职务名称
    @Update("update day.duty set duty_name=#{duty_name}  where duty_id=#{duty_id}  ")
    int updateDutyNameById(Duty duty);

    // 通过名称查询职务
    @Select("select * from day.duty where duty_name=#{duty_name}  ")
    Duty selectByName(Duty duty);

    // 添加职务
    @Insert("insert into day.duty(duty_name) values (#{duty_name}  )")
    int addDuty(Duty duty);

    // 查询职务员工数
    @Select("select count(*) from day.emp where duty_id = #{duty_id}")
    int getDutyEmployeeCount(int duty_id);

    // 删除职务
    @Delete("delete from day.duty where duty_id = #{duty_id}")
    int deleteDuty(int duty_id);
}