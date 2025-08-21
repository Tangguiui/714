package com.oa5.service;

import com.oa5.pojo.Duty;
import com.oa5.util.RESP;

public interface DutyService {

    RESP selectAllDeptAndNum1();
    RESP selectAllDeptAndNum(int current, int size);

    RESP addDuty(Duty duty, int current, int size);

    RESP updateDutyNameById(Duty duty, int current, int size);

    RESP deleteDuty(int duty_id, int current, int size);
}