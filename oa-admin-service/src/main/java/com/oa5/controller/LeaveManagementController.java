package com.oa5.controller;


import com.oa5.pojo.Leave;
import com.oa5.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveManagementController {

    @Autowired
    private LeaveService leaveService;

    // 获取所有员工请假记录
    @GetMapping("/all-leaves")
    public List<Leave> getAllLeaves() {
        System.out.println(leaveService.getAllLeaves());
        return leaveService.getAllLeaves();
    }

    // 审批员工请假申请
    @PostMapping("/approve/{leaveId}/{status}")
    public boolean approveLeave(@PathVariable String leaveId, @PathVariable String status) {
        return leaveService.approveLeave(leaveId, status);
    }
}
