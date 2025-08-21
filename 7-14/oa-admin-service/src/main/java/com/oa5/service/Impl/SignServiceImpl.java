package com.oa5.service.Impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oa5.dao.EmpDao;
import com.oa5.dao.SignDao;
import com.oa5.pojo.Sign;
import com.oa5.repository.SignElasticsearchRepository;
import com.oa5.service.SignService;
import com.oa5.util.DU;
import com.oa5.util.RESP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SignServiceImpl implements SignService {

    private String day="";

    @Autowired
    private SignDao signDao;
    @Autowired
    private EmpDao empDao;
    @Autowired
    private SignElasticsearchRepository signRepository;


    /*@Override
    public RESP getTodayAttendance(@RequestParam(name = "currentPage") int currentPage,
                                   @RequestParam(name = "pageSize") int pageSize) {
        try {
            String today = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

            // 1. 获取今日签到总人数
            int total = signDao.countToDayYes(today);

            // 2. 创建空数据的分页对象
            Page<Sign> page = new Page<>(currentPage, pageSize);
            page.setTotal(total); // 设置总记录数

            // 3. 创建分页信息对象
            PageInfo<Sign> pageInfo = new PageInfo<>(page);
            pageInfo.setTotal(total); // 确保总记录数正确

            // 4. 返回带分页信息的响应
            return RESP.ok(pageInfo, currentPage,total); // 直接返回总签到人数
        } catch (Exception e) {
            return RESP.error("获取今日签到人数失败：" + e.getMessage());
        }
    }*/
    @Override
    public RESP getTodayAttendance(@RequestParam(name = "currentPage") int currentPage,
                                   @RequestParam(name = "pageSize") int pageSize) {
        try {

            String today = DU.getNowSortString();

            List<Sign> list=new ArrayList<>();

            // 1. 查询当天打卡记录
            // 检查是否已经存在今日的签到记录（使用dateOnly字段）需要为当前员工创建签到任务
            long total = signRepository.countByDateOnlyAndStateAndType(today,"已签到","a");

            // 2. 创建空数据的分页对象
            Page<Sign> page = new Page<>(currentPage, pageSize);
            page.setTotal(total); // 设置总记录数

            // 3. 创建分页信息对象
            PageInfo<Sign> pageInfo = new PageInfo<>(page);
            pageInfo.setTotal(total); // 确保总记录数正确

            // 4. 返回带分页信息的响应
            return RESP.ok(pageInfo, currentPage,(int)total); // 直接返回总签到人数
        } catch (Exception e) {
            return RESP.error("获取今日签到人数失败：" + e.getMessage());
        }
    }


    @Override
    public RESP selectDaySignList(int current, int size) {
        try {
            // 1. 获取所有签到记录的日期（去重）
            List<Sign> allSigns = signRepository.findAllByOrderByTimestampDesc();
            System.out.println(allSigns);
            Set<String> dateSet = new LinkedHashSet<>(); // 保持插入顺序
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            for (Sign sign : allSigns) {
                if (sign.getSignDate() != null) {
                    // 提取日期部分（忽略时间）
                    String datePart = sdf.format(sdf.parse(sign.getSignDate()));
                    dateSet.add(datePart);
                }
            }

            // 2. 将日期转换为列表并排序（最新日期在前）
            List<String> dates = new ArrayList<>(dateSet);
            dates.sort(Comparator.reverseOrder());

            // 3. 计算分页信息
            int total = dates.size();
            int start = Math.min((current - 1) * size, total);
            int end = Math.min(start + size, total);

            // 4. 截取当前页的日期
            List<String> currentPageDates = new ArrayList<>();
            if (start < end) {
                currentPageDates = dates.subList(start, end);
            }

            // 5. 为当前页的每个日期查询已签到和未签到人数
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (String date : currentPageDates) {
                // 组装数据（使用Map代替VO）
                Map<String, Object> dayData = new HashMap<>();
                dayData.put("date", date);
                resultList.add(dayData);
            }

            // 6. 返回分页数据
            return RESP.ok(resultList, current, total);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RESP selectDayDetails(String date) {
        try {

            // 5. 为当前页的每个日期查询已签到和未签到人数
            Map<String,Integer> resultList = new HashMap<>();
                // 查询该日已签到人数
                int morningSignedCount = (int)signRepository.countByDateOnlyAndStateAndType(date,"已签到","a");
                // 查询该日未签到人数
                int morningUnsignedCount = (int)signRepository.countByDateOnlyAndStateAndType(date,"未签到","a");
                int eveningSignedCount=(int)signRepository.countByDateOnlyAndStateAndType(date,"已签到","p");
                int eveningUnsignedCount=(int)signRepository.countByDateOnlyAndStateAndType(date,"未签到","p");
                int totalSignedCount=morningSignedCount+eveningSignedCount;
                int totalUnsignedCount=morningUnsignedCount+eveningUnsignedCount;

                resultList.put("morningSignedCount",morningSignedCount);
                resultList.put("morningUnsignedCount",morningUnsignedCount);
                resultList.put("eveningSignedCount",eveningSignedCount);
                resultList.put("eveningUnsignedCount",eveningUnsignedCount);
                resultList.put("totalSignedCount",totalSignedCount);
                resultList.put("totalUnsignedCount",totalUnsignedCount);

            // 6. 返回分页数据
            return RESP.ok(resultList);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RESP selectYes(String date,String signType, int current, int size, String search_users) {
        try {
            if(search_users.equals("")) {
                // 1. 查询总记录数
                int totalCount = (int)signRepository.countByDateOnlyAndStateAndType(date,"已签到",signType);
                // 2. 计算分页参数
                int offset = (current - 1) * size;
                // 3. 获取当前页数据
                Pageable pageable = PageRequest.of(current - 1, size);
                org.springframework.data.domain.Page<Sign> page = signRepository.findByDateOnlyAndStateAndType(date,"已签到",signType,pageable);
                System.out.println(page);
                List<Sign> signList = page.getContent();
                System.out.println(signList);
                // 5. 构建响应
                return RESP.ok(signList, current, totalCount);
            }else{
                // 1. 查询总记录数
                int totalCount = (int)signRepository.countByDateOnlyAndStateAndType(date,"已签到",signType);
                // 3. 获取当前页数据

                List<Sign> signList = signRepository.findByDateOnlyAndStateAndType(date,"已签到",signType);
                List<Sign> filteredList = new ArrayList<>();
                for (Sign sign : signList) {
                    if (String.valueOf(sign.getNumber()).contains(search_users)) {
                        filteredList.add(sign);
                    }
                }
                // 手动分页计算
                int total = filteredList.size();
                int totalPages = (total + size - 1) / size; // 计算总页数
                int start = Math.min((current - 1) * size, total);
                int end = Math.min(start + size, total);
                List<Sign> pageData = filteredList.subList(start, end); // 截取当前页数据
                for(Sign sign:pageData){
                    if(!Objects.equals(sign.getSignDate(), DU.getYeaterdaySortString()) && !Objects.equals(sign.getSignDate(), DU.getNowSortString())) {
                        sign.setTag(0);
                    }
                }

                return RESP.ok(pageData, current, total);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public RESP selectNo(String date,String signType, int current, int size, String search_users) {
        try {
            if(search_users.equals("")) {
                // 1. 查询总记录数
                int totalCount = (int)signRepository.countByDateOnlyAndStateAndType(date,"未签到",signType);
                // 2. 计算分页参数
                int offset = (current - 1) * size;
                // 3. 获取当前页数据
                Pageable pageable = PageRequest.of(current - 1, size);
                org.springframework.data.domain.Page<Sign> page = signRepository.findByDateOnlyAndStateAndType(date,"未签到",signType,pageable);
                System.out.println(page);
                List<Sign> signList = page.getContent();
                System.out.println("yzy");
                System.out.println(signList);
                // 5. 构建响应
                return RESP.ok(signList, current, totalCount);
            }else{
                // 1. 查询总记录数
                int totalCount = (int)signRepository.countByDateOnlyAndStateAndType(date,"未签到",signType);
                // 3. 获取当前页数据

                List<Sign> signList = signRepository.findByDateOnlyAndStateAndType(date,"未签到",signType);
                List<Sign> filteredList = new ArrayList<>();
                for (Sign sign : signList) {
                    if (String.valueOf(sign.getNumber()).contains(search_users)) {
                        filteredList.add(sign);
                    }
                }
                // 手动分页计算
                int total = filteredList.size();
                int totalPages = (total + size - 1) / size; // 计算总页数
                int start = Math.min((current - 1) * size, total);
                int end = Math.min(start + size, total);
                List<Sign> pageData = filteredList.subList(start, end); // 截取当前页数据
                for(Sign sign:pageData){
                    if(!Objects.equals(sign.getSignDate(), DU.getYeaterdaySortString()) && !Objects.equals(sign.getSignDate(), DU.getNowSortString())) {
                        sign.setTag(0);
                    }
                }
                return RESP.ok(pageData, current, total);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public RESP approveMakeUp(String id) {
        try {
            signRepository.approveMakeUp(id);
            return RESP.ok("补签成功");
        } catch (Exception e) {
            return RESP.error("补签失败：" + e.getMessage());
        }
    }

    @Override
    public RESP rejectMakeUp(String id) {
        try {
            signRepository.rejectMakeUp(id);
            return RESP.ok("驳回成功");
        }catch (Exception e){
            return RESP.error("驳回失败：" + e.getMessage());
        }
    }
        /*@Override
        public RESP updateState(Sign sign, HttpSession session) {
            // 执行更新考勤状态的操作
            int result = signDao.updateState(sign, DU.getNowString());
            if (result > 0) {
                // 更新成功，返回更新后的数据
                PageHelper.startPage(1, 10); // 假设默认页码为 1，每页显示 10 条记录
                List<Sign> list = signDao.selectAll();
                PageInfo<Sign> data = new PageInfo<>(list);
                return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
            }
            return null;
        }*/

        @Override
        public RESP updateStateYes(Sign sign, int currentPage, int pageSize) {
            // 执行补签操作
            sign.setState("已签到"); // 设置补签状态为已签到
            int result = signDao.updateState(sign, DU.getNowString());
            if (result > 0) {
                // 补签成功，返回更新后的数据
                PageHelper.startPage(currentPage, pageSize);
                List<Sign> list = signDao.selectAll();
                PageInfo<Sign> data = new PageInfo<>(list);
                return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
            }
            return null;
        }

        @Override
        public RESP updateStateNo(Sign sign, int currentPage, int pageSize) {
            // 执行驳回签到操作
            sign.setState("未签到"); // 设置驳回状态为未签到
            int result = signDao.updateState(sign,DU.getNowString());
            if (result > 0) {
                // 驳回成功，返回更新后的数据
                PageHelper.startPage(currentPage, pageSize);
                List<Sign> list = signDao.selectAll();
                PageInfo<Sign> data = new PageInfo<>(list);
                return RESP.ok(data.getList(), data.getPageNum(), (int) data.getTotal());
            }
            return null;
        }


    @Override
    public boolean setday(String signDate){
        day=signDate;
        return true;
    }

    @Override
    public RESP selectImgSignList(){
        List<String> list = new ArrayList<String>();
        //查询出所有员工的考勤记录
        List<Sign> list1 = signRepository.findAllByOrderByTimestampDesc();
        if(list1.size()>0){
            for(Sign sign:list1){
                String str = sign.getSignDate();
                String[] arr = str.split("\\s+");
                list.add(arr[0]);
            }
            //提取签到日期并去重
            List<String> list2 = list.stream().distinct().collect(Collectors.toList());
            //提取最近五天
            List<String> list3 = list2.subList(Math.max(list2.size()-5,0),list2.size());
            //统计总人数
            int count = empDao.countUser();
            List<Integer> listYc = new ArrayList<>();//已签到人数
            List<Integer> listNc = new ArrayList<>();//未签到人数
            List<Integer> listNe = new ArrayList<>();//总人数
            for(String day:list3){//循环遍历最近五天的集合
                listYc.add((int)signRepository.countByDateOnlyAndStateAndType(day,"已签到","a"));
                listNc.add((int)signRepository.countByDateOnlyAndStateAndType(day,"未签到","a"));
                listNe.add(count);
            }
            return RESP.ok(list3,listYc,listNc,listNe);
        }
        return null;
    }

}