package com.oa5.service.Impl;


import com.oa5.dao.LeaveDao;
import com.oa5.pojo.Leave;
import com.oa5.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveDao leaveDao;

    @Override
    public List<Leave> getAllLeaves() {
        return leaveDao.getAllLeaves();
    }

    @Override
    public boolean approveLeave(String leaveId, String status) {
        int result = leaveDao.updateLeaveStatus(leaveId, status);
        return result > 0;
    }
}
