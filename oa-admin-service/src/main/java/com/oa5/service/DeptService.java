package com.oa5.service;

import com.oa5.pojo.Department;
import com.oa5.util.RESP;

public interface DeptService {

    RESP selectAllDeptAndNum1();
    RESP selectAllDeptAndNum(int currentPage, int pageSize);

    RESP addDept(Department department, int current, int size);

    RESP updateDeptNameById(Department department, int current, int size);

    RESP deleteDept(int dept_id, int current, int size);
}