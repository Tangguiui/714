package com.oa5.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oa5.dao.DeptDao;
import com.oa5.dao.EmpDao;
import com.oa5.pojo.Department;
import com.oa5.service.DeptService;
import com.oa5.util.JediPoolUtil;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptDao deptdao;
    @Autowired
    private EmpDao empdao;
    @Autowired
    private JediPoolUtil jediPoolUtil;

    @Override
    public RESP selectAllDeptAndNum1() {
        List<Department> list = deptdao.selectAllDeptAndNum();
        PageInfo<Department> data = new PageInfo<>(list);
        return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
    }

    @Override
    public RESP selectAllDeptAndNum(int current, int size) {
        PageHelper.startPage(current, size, true);
        List<Department> list = deptdao.selectAllDeptAndNum();
        PageInfo<Department> data = new PageInfo<>(list);
        return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
    }

    @Override
    public RESP addDept(Department department, int current, int size) {
        List<Department> list1;
        if (deptdao.selectByName(department) == null) {
            int count = deptdao.addDept(department);
            if (count > 0) {
                //3将更新后的数据写入到redis
                //3.1获取redis的客户端连接Jedis（连接池）
                Jedis jedis = jediPoolUtil.getJedis();
                //3.2删除redis中旧的数据
                jedis.del("Dept");
                //4重新查询更改后的数据
                list1 = empdao.getDeptData();
                //5将数据写入redis
                String jsonString = JSONObject.toJSONString(list1);
                jedis.set("Dept", jsonString);
            }
            PageHelper.startPage(current, size, true);
            List<Department> list = deptdao.selectAllDeptAndNum();
            PageInfo<Department> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return null;
    }

    @Override
    public RESP updateDeptNameById(Department department, int current, int size) {
        List<Department> list1;
        if (deptdao.selectByName(department) == null) {
            int i = deptdao.updateDeptNameById(department);
            if (i > 0) {
                //3将更新后的数据写入到redis
                //3.1获取redis的客户端连接Jedis（连接池）
                Jedis jedis = jediPoolUtil.getJedis();
                //3.2删除redis中旧的数据
                jedis.del("Dept");
                //4重新查询更改后的数据
                list1 = empdao.getDeptData();
                //5将数据写入redis
                String jsonString = JSONObject.toJSONString(list1);
                jedis.set("Dept", jsonString);
            }
            //6分页响应数据
            PageHelper.startPage(current, size, true);
            List<Department> list = deptdao.selectAllDeptAndNum();
            PageInfo<Department> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return null;
    }

    @Override
    public RESP deleteDept(int dept_id, int current, int size) {
        int employeeCount = deptdao.getDeptEmployeeCount(dept_id);
        if (employeeCount > 0) {
            return RESP.error("该部门有员工，不能删除");
        }
        int result = deptdao.deleteDept(dept_id);
        if (result > 0) {
            // 更新 Redis 数据
            Jedis jedis = jediPoolUtil.getJedis();
            jedis.del("Dept");
            List<Department> list1 = empdao.getDeptData();
            String jsonString = JSONObject.toJSONString(list1);
            jedis.set("Dept", jsonString);

            PageHelper.startPage(current, size, true);
            List<Department> list = deptdao.selectAllDeptAndNum();
            PageInfo<Department> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return RESP.error("删除部门失败");
    }
}