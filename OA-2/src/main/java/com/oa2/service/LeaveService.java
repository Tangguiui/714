package com.oa2.service;

import com.oa2.pojo.Leave;
import com.oa2.util.RESP;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface LeaveService {
    // 提交请假申请
    RESP submitLeave(Leave leave, HttpSession session);

    // 获取当前员工的请假记录
    RESP getMyLeaves(HttpSession session);
}