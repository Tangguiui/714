package com.oa5.controller;

import com.oa5.pojo.Duty;
import com.oa5.service.DutyService;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
public class DutyController {
    @Autowired
    private DutyService dutyService;

    @RequestMapping("/duties")
    @ResponseBody
    public RESP selectAllDutyAndNum(@RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return dutyService.selectAllDeptAndNum(Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @RequestMapping("/employees/duties")
    @ResponseBody
    public RESP selectAllDutyAndNum1() {
        return dutyService.selectAllDeptAndNum1();
    }

    @PostMapping("/addDuty")
    @ResponseBody
    public RESP addDuty(@RequestBody Duty duty, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return dutyService.addDuty(duty, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @PutMapping("/updateDutyNameById/{duty_id}")
    @ResponseBody
    public RESP updateDutyNameById(@PathVariable("duty_id") Integer duty_id, @RequestBody Duty duty, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        duty.setDuty_id(duty_id);
        return dutyService.updateDutyNameById(duty, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }

    @DeleteMapping("/deleteDuty/{duty_id}")
    @ResponseBody
    public RESP deleteDuty(@PathVariable("duty_id") Integer duty_id, @RequestParam(name = "currentPage") String currentPage, @RequestParam(name = "pageSize") String pageSize) {
        return dutyService.deleteDuty(duty_id, Integer.parseInt(currentPage), Integer.parseInt(pageSize));
    }
}