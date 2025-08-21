package com.oa5.controller;

import com.oa5.pojo.Sign;
import com.oa5.service.SignService;
import com.oa5.util.DU;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping
@CrossOrigin
public class SignController {
    @Autowired
    SignService signService;

    /*@RequestMapping("/selectDaySignList")
    @ResponseBody
    public RESP selectDaySignList(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize)
    {
        return signService.selectDaySignList(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }*/
    @RequestMapping("/attendance/today/signed")
    @ResponseBody
    public RESP getTodayAttendance(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize)
    {
        return signService.getTodayAttendance(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @RequestMapping("/attendance/daily-statistics")
    @ResponseBody
    public RESP selectDaySignList(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize)
    {
        return signService.selectDaySignList(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @RequestMapping("/attendance/daily-details")
    @ResponseBody
    public RESP selectDayDetails(@RequestParam(name="date") String date){
        return signService.selectDayDetails(date);
    }

    @PostMapping("/attendance/signed")
    @ResponseBody
    public RESP selectYes(@RequestParam(name="date") String date,@RequestParam(name="signType") String signType,@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize,@RequestParam(name="searchUsers") String searchUsers)
    {
        return signService.selectYes(date,signType,Integer.parseInt(currentPage), Integer.parseInt(pageSize),searchUsers);
    }

    @PostMapping("/attendance/today/signed")
    @ResponseBody
    public RESP selectTodayYes(@RequestParam(name="signType") String signType,@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize,@RequestParam(name="searchUsers") String searchUsers)
    {
        String day= DU.getNowSortString();
        return signService.selectYes(day,signType,Integer.parseInt(currentPage), Integer.parseInt(pageSize),searchUsers);
    }

    @PostMapping("/attendance/unsigned")
    @ResponseBody
    public RESP selectNo(@RequestParam(name="date") String date,@RequestParam(name="signType") String signType,@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize,@RequestParam(name="searchUsers") String searchUsers)
    {
        return signService.selectNo(date,signType,Integer.parseInt(currentPage), Integer.parseInt(pageSize),searchUsers);
    }

    @PostMapping("/attendance/today/unsigned")
    @ResponseBody
    public RESP selectTodayNo(@RequestParam(name="signType") String signType,@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize,@RequestParam(name="searchUsers") String searchUsers)
    {
        String day= DU.getNowSortString();
        return signService.selectNo(day,signType,Integer.parseInt(currentPage), Integer.parseInt(pageSize),searchUsers);
    }
    @PutMapping("/attendance/{id}/approve")
    @ResponseBody
    public RESP approveMakeUp(@PathVariable("id") String id) {
        return signService.approveMakeUp(id);
    }
    @PutMapping("/attendance/{id}/reject")
    @ResponseBody
    public RESP rejectMakeUp(@PathVariable("id") String id) {
        return signService.rejectMakeUp(id);
    }

    @RequestMapping("/attendance/statistics/chart")
    @ResponseBody
    public RESP selectImgSignList()
    {
        return signService.selectImgSignList();
    }



    //补签
    @RequestMapping("/updateStateYes")
    @ResponseBody
    public RESP updateStateYes(Sign sign , @RequestParam(name = "currentPage") String currentPage , @RequestParam(name = "pageSize") String pageSize) {
        return signService.updateStateYes(sign , Integer.parseInt(currentPage) , Integer.parseInt(pageSize));
    }

    //驳回签到
    @RequestMapping("/updateStateNo")
    @ResponseBody
    public RESP updateStateNo(Sign sign , @RequestParam(name = "currentPage") String currentPage , @RequestParam(name = "pageSize") String pageSize) {
        return signService.updateStateNo(sign , Integer.parseInt(currentPage) , Integer.parseInt(pageSize));
    }

}
