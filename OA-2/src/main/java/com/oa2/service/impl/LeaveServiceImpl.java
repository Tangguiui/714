package com.oa2.service.impl;

import com.oa2.dao.LeaveDao;
import com.oa2.pojo.Emp;
import com.oa2.pojo.Leave;
import com.oa2.service.LeaveService;
import com.oa2.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.UUID;

@Service
public class LeaveServiceImpl implements LeaveService {
    @Autowired
    private LeaveDao leaveDao;

    @Override
    public RESP submitLeave(Leave leave, HttpSession session) {
        Emp emp = (Emp) session.getAttribute("emp");
        if (emp == null) {
            return RESP.error("用户未登录");
        }
        leave.setId(UUID.randomUUID().toString());
        leave.setNumber(emp.getNumber());
        leave.setName(emp.getName());
        leave.setDept_name(emp.getDept_name());
        leave.setStatus("待审批");
        try {
            int result = leaveDao.addLeave(leave);
            if (result > 0) {
                return RESP.ok("请假申请提交成功");
            } else {
                return RESP.error("请假申请提交失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RESP.error("请假申请提交失败：" + e.getMessage());
        }
    }

    @Override
    public RESP getMyLeaves(HttpSession session) {
        Emp emp = (Emp) session.getAttribute("emp");
        if (emp == null) {
            return RESP.error("用户未登录");
        }
        try {
            List<Leave> leaves = leaveDao.getLeavesByNumber(emp.getNumber());
            return RESP.ok(leaves);
        } catch (Exception e) {
            e.printStackTrace();
            return RESP.error("获取请假记录失败：" + e.getMessage());
        }
    }
}