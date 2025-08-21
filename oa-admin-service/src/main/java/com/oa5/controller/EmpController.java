package com.oa5.controller;

import com.oa5.service.EmpService;
import com.oa5.dao.EmpDao;
import com.oa5.pojo.Emp;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
@CrossOrigin
public class EmpController {
    @Autowired
    private EmpDao empDao;
    @Autowired
    private EmpService empService;


    /*@RequestMapping("/employees")
    @ResponseBody
    public RESP selectByPage(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize,
                             @RequestParam(name = "queryType" ,required = false) String queryType,
                             @RequestParam(name = "searchUsers",required = false) String searchUsers) {
        RESP resp = empService.selectByPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize),queryType, searchUsers);
        return resp;
    }*/
    @RequestMapping("/employees")
    @ResponseBody
    public RESP selectByPage(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize,
                             @RequestParam(name = "searchUsers",required = false) String searchUsers) {
        RESP resp = empService.selectByPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), searchUsers);
        return resp;
    }


    //获取部门信息-下拉列表
    @RequestMapping("/getDeptData")
    @ResponseBody
    public RESP getDeptData() {
        //  return empService.getDeptData();
        return empService.getDeptDataFinDall();
    }

    //获取职务信息-下拉列表
    @RequestMapping("/getDutyData")
    @ResponseBody
    public RESP getDutyData() {
        return empService.getDutyData();
    }

    // 添加用户
    @RequestMapping("/addEmp")
    @ResponseBody
    public RESP addEmp(@RequestBody Emp emp, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return empService.addEmp(emp, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    //更新用户信息
    @PutMapping("/updateEmp/{number}")
    @ResponseBody
    public RESP updateEmp(@PathVariable("number") Integer number, @RequestBody Emp emp,
                          @RequestParam(name = "currentPage") String currentPage,
                          @RequestParam(name = "pageSize") String pageSize) {
        // 确保 Emp 对象中的 number 与 URL 中的 number 一致
        emp.setNumber(number);
        return empService.updateEmp(emp, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    // 删除用户
    @DeleteMapping("/delete/{number}")
    @ResponseBody
    public RESP deleteEmp(@PathVariable("number") Integer number,
                          @RequestParam(name = "currentPage") String currentPage,
                          @RequestParam(name = "pageSize") String pageSize) {
        Emp emp = new Emp();
        emp.setNumber(number);
        return empService.deleteEmp(emp, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    //更新用户的人事信息
    @RequestMapping("/updateDD")
    @ResponseBody
    public RESP updateDD(@RequestBody Emp emp, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return empService.updateDD(emp, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }


}
