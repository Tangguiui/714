package com.oa2.service.impl;

import com.github.pagehelper.PageInfo;
import com.oa2.dao.EmpDao;
import com.oa2.pojo.Emp;
import com.oa2.pojo.Sign;
import com.oa2.repository.SignElasticsearchRepository;
import com.oa2.service.SignService;
import com.oa2.util.DU;
import com.oa2.util.LocationUtil;
import com.oa2.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class SignServiceElasticsearchImpl implements SignService {

    @Autowired
    private SignElasticsearchRepository signRepository;

    @Autowired
    private EmpDao empDao;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public RESP empSignList(HttpSession session) {
        System.out.println("获取当前员工签到记录");
        // 1. 获取当前员工数据
        Emp emp = (Emp) session.getAttribute("emp");
        if (emp == null) {
            return RESP.error("用户未登录");
        }

        // 2. 获取当天日期
        String today = DU.getNowSortString();

        // 3. 查询所有员工的当天打卡记录（只需要判断是否存在）
        long todayRecordCount = signRepository.countByDateOnly(today);
        System.out.println("今日所有员工签到记录总数: " + todayRecordCount);

        // 如果没有记录，为所有员工创建上下午各一条记录
        if (todayRecordCount == 0) {
            System.out.println("开始为所有员工创建今日签到记录...");
            List<Integer> allEmpNumbers = empDao.selectAllEmpNumber();

            for (int empNumber : allEmpNumbers) {
                // 创建上午签到记录
                Sign morSign = createSign(empNumber, DU.getNowAM(), "未签到", "a");
                signRepository.save(morSign);

                // 创建下午签到记录
                Sign afterSign = createSign(empNumber, DU.getNowPM(), "未签到", "p");
                signRepository.save(afterSign);
            }

            System.out.println("已为 " + allEmpNumbers.size() + " 名员工创建签到记录");
        }

        // 4. 查询当前员工的当天打卡记录（无论是否新建都需要查询）
        List<Sign> todayRecords = signRepository.findByNumberAndDateOnly(emp.getNumber(), today);
        System.out.println("当前员工今日签到记录数量: " + todayRecords.size());

        // 补充员工信息
        List<Sign> resultList = AddEmpInfo(todayRecords);
        System.out.println("最终返回记录: " + resultList);

        return RESP.ok(resultList);
    }
    // 获取当前员工签到记录
    /*@Override
    public RESP empSignList(HttpSession session) {

        System.out.println("获取当前员工签到记录");
        // 1. 获取当前员工数据
        Emp emp = (Emp) session.getAttribute("emp");
        if (emp == null) {
            return RESP.error("用户未登录");
        }
        // 2. 获取员工的当天的日期
        String today = DU.getNowSortString();

        List<Sign> list=new ArrayList<>();


        // 3. 查询当天打卡记录
        // 检查是否已经存在今日的签到记录（使用dateOnly字段）需要为当前员工创建签到任务
        List<Integer> list1=empDao.selectAllEmpNumber();
            List<Sign> TodayRecords = signRepository.findByNumberAndDateOnly(emp.getNumber(), today);
            System.out.println(TodayRecords.size());

            if (TodayRecords.isEmpty()) {
                for(int empNumber : list1) {
                    // 只为当前员工创建今日的签到记录
                    // 创建上午签到记录
                    Sign morSign = createSign(empNumber, DU.getNowAM(), "未签到", "a");
                    signRepository.save(morSign);

                    // 创建下午签到记录
                    Sign afterSign = createSign(empNumber, DU.getNowPM(), "未签到", "p");
                    signRepository.save(afterSign);

                    // 重新查询当天打卡记录
                    list = signRepository.findByNumberAndDateOnly(empNumber, today);
                }
            } else {
                list = TodayRecords;
            }
        // 补充员工信息（姓名、部门）
        list = AddEmpInfo(list);
System.out.println("listtttttttt");
System.out.println(list);
        return RESP.ok(list);
    }*/

    /**
     * 创建签到记录
     */
    private Sign createSign(int empNumber, String signDate, String state, String type) {
        Sign sign = new Sign();
        sign.setId(UUID.randomUUID().toString());
        sign.setSignDate(signDate);
        sign.setNumber(empNumber);
        sign.setState(state);
        sign.setType(type);
        sign.setName(empDao.selectByEmpNumber(empNumber).getName());
        sign.setDept_name(empDao.selectByEmpNumber(empNumber).getDept_name());
        sign.setTimestamp(System.currentTimeMillis());
        sign.setDateOnly(dateFormat.format(new Date()));
        sign.setTag(1);
        return sign;
    }

    /**
     * 补充签到记录的员工信息（姓名、部门）
     */
    private List<Sign> AddEmpInfo(List<Sign> signs) {
        for (Sign sign : signs) {
            try {
                // 根据员工编号查询员工信息
                Emp emp = empDao.selectByEmpNumber(sign.getNumber());
                if (emp != null) {
                    sign.setName(emp.getName());
                    sign.setDept_name(emp.getDept_name());
                }
            } catch (Exception e) {
                // 如果查询失败，设置默认值
                sign.setName("未知员工");
                sign.setDept_name("未知部门");
            }
        }
        return signs;
    }



    @Override
    public RESP selectByPagehelper(int currentPage, int pageSize, HttpSession session) {
        return selectByPage(currentPage, pageSize, session);
    }

    //分页查询员工已签到记录
    @Override
    public RESP selectByPage(int currentPage, int pageSize, HttpSession session) {
        // 1. 查询当前员工信息
        Emp emp = (Emp) session.getAttribute("emp");
        if (emp == null) {
            return RESP.error("用户未登录");
        }
        // 2. 使用 Elasticsearch 分页查询
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Sign> page = signRepository.findByNumberOrderByTimestampDesc(emp.getNumber(), pageable);

        //测试Page集合中数据 例如： 员工编号: 145, 签到时间: 2025-07-02 09:43:25:913, 状态: 已签到 ......
        List<Sign> list = page.getContent();
        for (int i = 0; i <list.size(); i++) {
            System.out.println("员工编号: " + list.get(0).getNumber() +
                               " 签到时间: " + list.get(0).getSignDate() +
                               " 状态: " + list.get(0).getState());
        }

        // 补充员工信息(补充对应的部门信息)
        list = AddEmpInfo(list);
        // 获取总条数
        long total = signRepository.countByNumber(emp.getNumber());

        return RESP.ok(list,currentPage, (int) total);
    }


    @Override
    public RESP updateState(Sign sign, HttpSession session, String coordinates) {
        try {
            Emp emp = (Emp) session.getAttribute("emp");
            if (emp == null) {
                return RESP.error("用户未登录");
            }
            // 设置员工编号
            sign.setNumber(emp.getNumber());
            // 查找今天该员工该类型的签到记录

            String today = DU.getNowSortString(); // 获取今天日期 yyyy-MM-dd

            // 先查询今天所有记录，然后筛选类型
            List<Sign> todayRecords = signRepository.findByNumberAndDateOnly(emp.getNumber(), today);
            System.out.println("签到的时候 查询记录");
            for (int i = 0; i <todayRecords.size(); i++) {
                System.out.println("员工编号: " + todayRecords.get(i).getNumber() +
                        ""+"签到时间: " + todayRecords.get(i).getSignDate() +
                        ""+"状态: " + todayRecords.get(i).getState());
            }

            Sign SignModer = null;
            // 查找对应类型的记录
            for (Sign record : todayRecords) {
                System.out.println(record.getType());
                if (sign.getType().equals(record.getType())) {
                    SignModer = record;
                    break;
                }
            }

            if (SignModer != null) {
                // 检查是否已经签到过
                if ("已签到".equals(SignModer.getState())) {
                    return RESP.error("今日已" + (sign.getType().equals("a") ? "签到" : "签退") + "，不可重复操作");
                }
                
                // 更新签到状态
                SignModer.setState("已签到");
                SignModer.setSignDate(DU.formatDateToString(new Date()));
                
                // 解析地理位置
                if (coordinates != null && !coordinates.isEmpty()) {
                    // 验证坐标格式
                    if (LocationUtil.isValidCoordinates(coordinates)) {
                        try {
                            String address = LocationUtil.getAddressFromCoordinates(coordinates);
                            SignModer.setSign_address(address);
                        } catch (Exception e) {
                            System.err.println("地址解析异常：" + e.getMessage());
                            SignModer.setSign_address("位置解析失败");
                        }
                    } else {
                        SignModer.setSign_address("坐标格式错误");
                    }
                } else {
                    SignModer.setSign_address("未获取到位置信息");
                }

                // 更新时间戳
                SignModer.setTimestamp(System.currentTimeMillis());
                SignModer.setDateOnly(today);

                // 补充员工信息
                SignModer.setName(emp.getName());
                SignModer.setDept_name(emp.getDept_name());

                signRepository.save(SignModer);
                return RESP.ok((sign.getType().equals("a") ? "签到" : "签退") + "成功");
            } else {
                return RESP.error("未找到今日的" + (sign.getType().equals("a") ? "签到" : "签退") + "任务，请联系管理员");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RESP.error("签到失败：" + e.getMessage());
        }
    }
}