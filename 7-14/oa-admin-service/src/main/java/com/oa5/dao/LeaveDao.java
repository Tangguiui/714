package com.oa5.dao;

import com.oa5.pojo.Leave;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LeaveDao {
    // 获取所有员工请假记录
    @Select("SELECT * FROM day.leave")
    List<Leave> getAllLeaves();

    // 根据 ID 获取请假记录
    @Select("SELECT * FROM `leave` WHERE id = #{id}")
    Leave getLeaveById(String id);

    // 更新请假记录状态
    @Update("UPDATE `leave` SET status = #{status} WHERE id = #{id}")
    int updateLeaveStatus(@Param("id") String id, @Param("status") String status);

    // 插入新的请假记录
    @Insert("INSERT INTO `leave` (id, number, name, dept_name, startDate, endDate, reason, status) " +
            "VALUES (#{id}, #{number}, #{name}, #{dept_name}, #{startDate}, #{endDate}, #{reason}, #{status})")
    int insertLeave(Leave leave);

    // 根据员工编号统计请假次数
    @Select("SELECT COUNT(*) FROM `leave` WHERE number = #{number}")
    int countLeavesByNumber(int number);

    // 根据状态获取请假记录
    @Select("SELECT * FROM `leave` WHERE status = #{status}")
    List<Leave> getLeavesByStatus(String status);
}