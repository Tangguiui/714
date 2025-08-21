package com.oa5.service;


import com.oa5.pojo.Leave;

import java.util.List;

public interface LeaveService {
    // 获取所有员工请假记录
    List<Leave> getAllLeaves();

    // 审批员工请假申请
    boolean approveLeave(String leaveId, String status);
}
