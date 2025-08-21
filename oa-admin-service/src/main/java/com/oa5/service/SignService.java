package com.oa5.service;

import com.oa5.pojo.Sign;
import com.oa5.util.RESP;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

public interface SignService {
    RESP getTodayAttendance(@RequestParam(name = "currentPage") int currentPage, @RequestParam(name = "pageSize") int pageSize);

    RESP selectDaySignList(int current,int size);

    RESP selectDayDetails(@RequestParam(name="date") String date);

    RESP selectYes(String date,String signType,int currentPage, int pageSize,String searchUsers);

    RESP selectNo(String date,String signType,int currentPage, int pageSize,String searchUsers);

    boolean setday(String  signDate);

    RESP approveMakeUp(String id);

    RESP rejectMakeUp(String id);
    //RESP updateState(Sign sign, HttpSession session);

    RESP updateStateYes(Sign sign, int currentPage, int pageSize);

    RESP updateStateNo(Sign sign, int currentPage, int pageSize);

    RESP selectImgSignList();


}
