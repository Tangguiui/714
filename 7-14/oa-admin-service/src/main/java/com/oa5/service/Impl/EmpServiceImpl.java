package com.oa5.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oa5.pojo.Department;
import com.oa5.pojo.Duty;
import com.oa5.dao.EmpDao;
import com.oa5.pojo.Emp;
import com.oa5.service.EmpService;
import com.oa5.util.JediPoolUtil;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;


@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpDao empDao;
    @Autowired
    private JediPoolUtil jediPoolUtil;

    //查询所有员工信息1
    @Override
    public RESP selectByPage(int current, int size, String keyword) {
        // 情况1：无关键字时直接使用PageHelper分页
        if (StringUtils.isEmpty(keyword)) {
            PageHelper.startPage(current, size, true);
            List<Emp> list = empDao.selectByPageHelper();
            PageInfo<Emp> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }

        // 情况2：有关键字时，先获取全量数据再手动分页
        List<Emp> allEmps = empDao.selectByPageHelper(); // 获取全部数据
        List<Emp> filteredList = new ArrayList<>();

        // 根据查询类型过滤数据
            for (Emp emp : allEmps) {
                if (emp.getName().contains(keyword)) {
                    filteredList.add(emp);
                }
            }
        /*// 根据查询类型过滤数据
        if ("name".equals(queryType)) {
            for (Emp emp : allEmps) {
                if (emp.getName().contains(keyword)) {
                    filteredList.add(emp);
                }
            }
        } else if ("dept".equals(queryType)) {
            for (Emp emp : allEmps) {
                if (emp.getDept_name().contains(keyword)) {
                    filteredList.add(emp);
                }
            }
        }*/

        // 手动分页计算
        int total = filteredList.size();
        int totalPages = (total + size - 1) / size; // 计算总页数
        int start = Math.min((current - 1) * size, total);
        int end = Math.min(start + size, total);
        List<Emp> pageData = filteredList.subList(start, end); // 截取当前页数据

        return RESP.ok(pageData, current, total);
    }

    //获取部门信息2
    @Override
    public RESP getDeptDataFinDall() {
        Jedis jedis = jediPoolUtil.getJedis();
        String dept_json = jedis.get("Dept");
        //没有数据  从数据库获取
        List<Department> lists;
        try {
            if (dept_json == null || dept_json.length() == 0) {
                System.out.println("redis没有部门数据，从数据库中进行读取......");
                //返回查询到的结果
                lists = empDao.getDeptData();

                //将结果写入到Redis中
                String jsonString = JSONObject.toJSONString(lists);
                jedis.set("Dept", jsonString);
            } else {
                //直接从Redis中读取数据
                lists = JSONObject.parseArray(dept_json, Department.class);
                System.out.println("直接查询Redis中的部门......");
            }
        }finally {
            //归还连接
            jedis.close();
        }

        return RESP.ok(lists);
    }

    //    获取职务列表3
    @Override
    public RESP getDutyData() {
        Jedis jedis = jediPoolUtil.getJedis();
        String dept_json = jedis.get("Duty");

        //没有数据  从数据库获取
        List<Duty> Duty_lists;
        if (dept_json == null || dept_json.length() == 0) {
            System.out.println("redis没有职务数据，从数据库中进行读取......");
            //返回查询到的结果
            Duty_lists = empDao.getDutyData();

            //将结果写入到Redis中
            String jsonString = JSONObject.toJSONString(Duty_lists);
            jedis.set("Duty", jsonString);
            //归还连接
            jedis.close();
        } else {
            //直接从Redis中读取数据
            Duty_lists = JSONObject.parseArray(dept_json, Duty.class);
            System.out.println("直接查询Redis中的职务......");
        }
        return RESP.ok(Duty_lists);
    }

    //    添加员工4
    @Override
    public RESP addEmp(Emp emp, int current, int size) {
        Emp emp1 = empDao.selectByName(emp);
        if (emp1 != null) {
            return null;
        } else {
            int r = empDao.addEmp(emp);
            if (r > 0) {
                System.out.println("测试新增数据...");
                PageHelper.startPage(current, size, true);
                List<Emp> list = empDao.selectByPageHelper();
                PageInfo<Emp> data = new PageInfo<>(list);
                return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
            }
        }
        return null;
    }


    //更新员工信息5
    @Override
    public RESP updateEmp(Emp emp, int current, int size) {
        int r = empDao.updateEmp(emp);
        int l = empDao.updateDD(emp);
        if (r > 0 && l>0) {
            System.out.println("测试修改数据...");
            PageHelper.startPage(current, size, true);
            List<Emp> list = empDao.selectByPageHelper();
            PageInfo<Emp> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return null;
    }

    //    删除员工信息 6
    @Override
    public RESP deleteEmp(Emp emp,int current, int size) {
        int d = empDao.deleteEmpSignByNumber(emp);
        if (d >= 0) {
            int r = empDao.deleteEmp(emp);
            if (r > 0) {
                System.out.println("测试删除数据...");
                PageHelper.startPage(current, size, true);
                List<Emp> list = empDao.selectByPageHelper();
                PageInfo<Emp> data = new PageInfo<>(list);
                return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
            }
        }
        return null;
    }


    //    更新人事信息 7
    @Override
    public RESP updateDD(Emp emp, int current, int size) {
        System.out.println("实现修改员工职务变动");
        int r = empDao.updateDD(emp);
        if (r > 0) {
            PageHelper.startPage(current, size, true);
            List<Emp> list = empDao.selectByPageHelper();
            PageInfo<Emp> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return null;
    }


}
