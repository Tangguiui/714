package com.oa5.service;

import com.oa5.pojo.Admin;
import com.oa5.util.RESP;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;


public interface AdmService {
    //管理员登录
    String login(Admin admin, HttpSession session);
    //管理员注册
    String register(Admin admin);

    RESP getEmployeeCount(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize);

    RESP getDepartmentCount();

    RESP getDutyCount();

    RESP getTodayAttendance(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize);


}
