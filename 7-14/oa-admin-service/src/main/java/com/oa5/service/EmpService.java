package com.oa5.service;

import com.github.pagehelper.Page;
import com.oa5.pojo.Emp;
import com.oa5.util.RESP;

import javax.servlet.http.HttpSession;



public interface EmpService {

    //查询所有员工信息
    RESP selectByPage(int current, int size,String keyword);

    //获取REDIS重的部门列表
    RESP getDeptDataFinDall();

    //获取职务列表
    RESP getDutyData();

    //添加员工
    RESP addEmp(Emp emp, int current, int size);

    //更新员工信息
    RESP updateEmp(Emp emp, int current, int size);

    //删除员工信息
    RESP deleteEmp(Emp emp, int current, int size);

    //更新员人事信息
    RESP updateDD(Emp emp, int current, int size);



}
