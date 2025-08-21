package com.oa5.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oa5.dao.DutyDao;
import com.oa5.dao.EmpDao;
import com.oa5.pojo.Duty;
import com.oa5.service.DutyService;
import com.oa5.util.JediPoolUtil;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.List;

@Service
public class DutyServiceImpl implements DutyService {
    @Autowired
    private DutyDao dutydao;
    @Autowired
    private EmpDao empdao;
    @Autowired
    private JediPoolUtil jediPoolUtil;

    @Override
    public RESP selectAllDeptAndNum1() {
        List<Duty> list = dutydao.selectAllDutyAndNum();
        PageInfo<Duty> data = new PageInfo<>(list);
        return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
    }

    @Override
    public RESP selectAllDeptAndNum(int current, int size) {
        PageHelper.startPage(current, size, true);
        List<Duty> list = dutydao.selectAllDutyAndNum();
        PageInfo<Duty> data = new PageInfo<>(list);
        return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
    }

    @Override
    public RESP addDuty(Duty duty, int current, int size) {
        List<Duty> list1;
        if (dutydao.selectByName(duty) == null) {
            int count = dutydao.addDuty(duty);
            if (count > 0) {
                //3将更新后的数据写入到redis
                //3.1获取redis的客户端连接Jedis（连接池）
                Jedis jedis = jediPoolUtil.getJedis();
                //3.2删除redis中旧的数据
                jedis.del("Duty");
                //4重新查询更改后的数据
                list1 = empdao.getDutyData();
                //5将数据写入redis
                String jsonString = JSONObject.toJSONString(list1);
                jedis.set("Duty", jsonString);
            }
            PageHelper.startPage(current, size, true);
            List<Duty> list = dutydao.selectAllDutyAndNum();
            PageInfo<Duty> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return null;
    }

    @Override
    public RESP updateDutyNameById(Duty duty, int current, int size) {
        List<Duty> list1;
        if (dutydao.selectByName(duty) == null) {
            int i = dutydao.updateDutyNameById(duty);
            if (i > 0) {
                //3将更新后的数据写入到redis
                //3.1获取redis的客户端连接Jedis（连接池）
                Jedis jedis = jediPoolUtil.getJedis();
                //3.2删除redis中旧的数据
                jedis.del("Duty");
                //4重新查询更改后的数据
                list1 = empdao.getDutyData();
                //5将数据写入redis
                String jsonString = JSONObject.toJSONString(list1);
                jedis.set("Duty", jsonString);
            }
            //6分页响应数据
            PageHelper.startPage(current, size, true);
            List<Duty> list = dutydao.selectAllDutyAndNum();
            PageInfo<Duty> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return null;
    }

    @Override
    public RESP deleteDuty(int duty_id, int current, int size) {
        int employeeCount = dutydao.getDutyEmployeeCount(duty_id);
        if (employeeCount > 0) {
            return RESP.error("该职务有员工，不能删除");
        }
        int result = dutydao.deleteDuty(duty_id);
        if (result > 0) {
            // 更新 Redis 数据
            Jedis jedis = jediPoolUtil.getJedis();
            jedis.del("Duty");
            List<Duty> list1 = empdao.getDutyData();
            String jsonString = JSONObject.toJSONString(list1);
            jedis.set("Duty", jsonString);

            PageHelper.startPage(current, size, true);
            List<Duty> list = dutydao.selectAllDutyAndNum();
            PageInfo<Duty> data = new PageInfo<>(list);
            return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
        }
        return RESP.error("删除职务失败");
    }
}