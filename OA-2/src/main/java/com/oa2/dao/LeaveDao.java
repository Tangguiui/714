package com.oa2.dao;

import com.oa2.pojo.Leave;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LeaveDao {
    // 添加请假记录
    @Insert("INSERT INTO day.leave (id, number, name, dept_name, start_date, end_date, reason, status) " +
            "VALUES (#{id}, #{number}, #{name}, #{dept_name}, STR_TO_DATE(#{startDate}, '%Y-%m-%d %H:%i:%s'), STR_TO_DATE(#{endDate}, '%Y-%m-%d %H:%i:%s'), #{reason}, #{status})")
    int addLeave(Leave leave);

    // 根据员工工号查询请假记录
    @Select("SELECT id, number, name, dept_name, DATE_FORMAT(start_date, '%Y-%m-%d %H:%i:%s') as startDate, DATE_FORMAT(end_date, '%Y-%m-%d %H:%i:%s') as endDate, reason, status FROM day.leave WHERE number = #{number}")
    List<Leave> getLeavesByNumber(int number);
}