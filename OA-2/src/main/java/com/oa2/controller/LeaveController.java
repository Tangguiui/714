package com.oa2.controller;

import com.oa2.pojo.Leave;
import com.oa2.service.LeaveService;
import com.oa2.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/leave")
@CrossOrigin
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping("/submit")
    public RESP submitLeave(@RequestBody LeaveRequest leaveRequest, HttpSession session) {
        Leave leave = new Leave();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println("接收到的开始日期: " + leaveRequest.getStartDate());
            System.out.println("接收到的结束日期: " + leaveRequest.getEndDate());
            leave.setStartDate(sdf.parse(leaveRequest.getStartDate()));
            leave.setEndDate(sdf.parse(leaveRequest.getEndDate()));
            leave.setReason(leaveRequest.getReason());
            return leaveService.submitLeave(leave, session);
        } catch (ParseException e) {
            System.out.println("日期解析错误: " + e.getMessage());
            return RESP.error("日期格式不正确，请使用 yyyy-MM-dd HH:mm:ss 格式");
        }
    }

    @GetMapping("/my-leaves")
    public RESP getMyLeaves(HttpSession session) {
        return leaveService.getMyLeaves(session);
    }

    // 内部类用于接收请求数据
    private static class LeaveRequest {
        private String startDate;
        private String endDate;
        private String reason;

        // getters and setters
        public String getStartDate() { return startDate; }
        public void setStartDate(String startDate) { this.startDate = startDate; }
        public String getEndDate() { return endDate; }
        public void setEndDate(String endDate) { this.endDate = endDate; }
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
}