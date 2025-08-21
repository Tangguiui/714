package com.oa5.pojo;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class Leave {
    private String id;
    private int number;
    private String name;
    private String dept_name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date start_date;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end_date;

    private String reason;
    private String status;

    public Leave() {}

    public Leave(String id, int number, String name, String dept_name, Date startDate, Date endDate, String reason, String status) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.dept_name = dept_name;
        this.start_date = startDate;
        this.end_date = endDate;
        this.reason = reason;
        this.status = status;
    }

    // Getter 和 Setter 方法
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDept_name() {
        return dept_name;
    }

    public void setDept_name(String dept_name) {
        this.dept_name = dept_name;
    }

    public Date getStartDate() {
        return start_date;
    }

    public void setStartDate(Date startDate) {
        this.start_date = startDate;
    }

    public Date getEndDate() {
        return end_date;
    }

    public void setEndDate(Date endDate) {
        this.end_date = endDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
