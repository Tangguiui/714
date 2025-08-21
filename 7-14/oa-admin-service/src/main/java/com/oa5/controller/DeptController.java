package com.oa5.controller;

import com.oa5.pojo.Department;
import com.oa5.service.DeptService;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
public class DeptController {
    @Autowired
    private DeptService deptService;

    @RequestMapping("/departments")
    @ResponseBody
    public RESP selectAllDeptAndNum(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return deptService.selectAllDeptAndNum(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @RequestMapping("/employees/departments")
    @ResponseBody
    public RESP selectAllDeptAndNum1() {
        return deptService.selectAllDeptAndNum1();
    }

    @PostMapping("/addDept")
    @ResponseBody
    public RESP addDept(@RequestBody Department dept, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return deptService.addDept(dept, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @PutMapping("/updateDeptNameById/{dept_id}")
    @ResponseBody
    public RESP updateDeptNameById(@PathVariable("dept_id") Integer dept_id, @RequestBody Department dept, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        dept.setDept_id(dept_id);
        return deptService.updateDeptNameById(dept, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @DeleteMapping("/deleteDept/{dept_id}")
    @ResponseBody
    public RESP deleteDept(@PathVariable("dept_id") Integer dept_id, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return deptService.deleteDept(dept_id, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }
}