package com.oa5.controller;

import com.oa5.service.AdmService;
import com.oa5.pojo.Admin;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping
@CrossOrigin
public class AdmController {

    @Autowired
    private AdmService admService;

//    管理员登录
    @PostMapping("/login")
    public String login(@RequestBody Admin admin, HttpSession session) {
        return admService.login(admin,session);
    }


    //    管理员注册
    @PostMapping("/register")
    public String register(@RequestBody Admin admin) {
        return admService.register(admin);
    }

    /**
     * 获取当前登录管理员信息
     */
    @GetMapping("/profile")
    public RESP getProfile(HttpSession session) {
        Admin admin = (Admin) session.getAttribute("admin");
        return RESP.ok(admin);
    }

    /**
     * 管理员退出登录
     */
    @PostMapping("/logout")
    public RESP logout(HttpSession session) {
        session.removeAttribute("admin");
        return RESP.ok("退出登录成功");
    }
/*
    @RequestMapping("/employees")
    @ResponseBody
    public RESP getEmployeeCount(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize) {
        return admService.getEmployeeCount(currentPage, pageSize);
    }

    @RequestMapping("/departments")
    @ResponseBody
    public RESP getDepartmentCount() {
        return admService.getDepartmentCount();
    }

    @RequestMapping("/duties")
    @ResponseBody
    public RESP getDutyCount() {
        return admService.getDutyCount();
    }

    @RequestMapping("/attendance/today/signed")
    @ResponseBody
    public RESP getTodayAttendance(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize) {
        return admService.getTodayAttendance(currentPage, pageSize);
    }*/
}
