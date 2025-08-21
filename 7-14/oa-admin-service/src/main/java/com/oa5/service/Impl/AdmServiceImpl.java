package com.oa5.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.oa5.dao.*;
import com.oa5.pojo.*;
import com.oa5.service.AdmService;
import com.liuvei.common.SysFun;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class AdmServiceImpl implements AdmService {

    @Autowired
    private AdmDao admDao;
    @Autowired
    private EmpDao empDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private DutyDao dutyDao;
    @Autowired
    private SignDao signDao;

    //管理员登录
    @Override
    public String login(Admin admin, HttpSession session) {
        Admin admin1 = admDao.selectByName(admin);
        if (admin1 != null) {
            if (admin1.getPwd().equals(SysFun.md5(admin.getPwd()))) {
                session.setAttribute("admin",admin1);
                return "true";
            }
        }
        return "false";
    }

    //管理员注册
    @Override
    public String register(Admin admin) {
        Admin admin1 = admDao.selectByName(admin);
        if (admin1 == null) {
            admin.setPwd(SysFun.md5(admin.getPwd()));
            int a = admDao.insertAdm(admin);
            if (a > 0) {
                return "true";
            }
        }
        return "false";
    }

    @Override
    public RESP getEmployeeCount(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize){
        try {
            int total = empDao.countUser();
            PageInfo<Emp> pageInfo = new PageInfo<>(new Page<>());
            pageInfo.setTotal(total);
            return RESP.ok(pageInfo);
        } catch (Exception e) {
            return RESP.error("获取员工总数失败：" + e.getMessage());
        }
    }

    @Override
    public RESP getDepartmentCount(){
        try {
            List<Department> departments = deptDao.selectAllDeptAndNum();
            return RESP.ok(departments);
        } catch (Exception e) {
            return RESP.error("获取部门数量失败：" + e.getMessage());
        }
    }

    @Override
    public RESP getDutyCount(){
        try {
            List<Duty> duties = dutyDao.selectAllDutyAndNum();
            return RESP.ok(duties);
        } catch (Exception e) {
            return RESP.error("获取职务数量失败：" + e.getMessage());
        }
    }

    @Override
    public RESP getTodayAttendance(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize){
        try {
            String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
            int total = signDao.countToDayYes(today);
            PageInfo<Sign> pageInfo = new PageInfo<>(new Page<>());
            pageInfo.setTotal(total);
            return RESP.ok(pageInfo);
        } catch (Exception e) {
            return RESP.error("获取今日签到人数失败：" + e.getMessage());
        }
    }
}
