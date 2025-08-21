package com.oa5.repository;

import com.oa5.pojo.Sign;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignElasticsearchRepository extends ElasticsearchRepository<Sign, String> {
    
    /**
     * 根据员工编号和日期查询打卡记录
     */
    @Query("{\"bool\":{\"must\":[{\"term\":{\"number\":\"?0\"}},{\"wildcard\":{\"signDate\":\"?1*\"}}]}}")
    List<Sign> findByNumberAndSignDateStartsWith(int number, String datePrefix);
    
    /**
     * 根据员工编号查询打卡记录（分页）
     */
    Page<Sign> findByNumberOrderByTimestampDesc(int number, Pageable pageable);
    
    /**
     * 查询所有记录（分页）
     */
    Page<Sign> findAllByOrderByTimestampDesc(Pageable pageable);
    
    /**
     * 统计员工的总打卡记录数
     */
    long countByNumber(int number);
    
    /**
     * 统计所有打卡记录数
     */
    long count();
    
    /**
     * 根据状态查询记录（分页）
     */
    Page<Sign> findByState(String state, Pageable pageable);
    
    /**
     * 统计指定状态的记录数
     */
    long countByState(String state);

    /**
     * 根据日期前缀查询记录（分页）
     */
    Page<Sign> findBySignDateStartsWith(String datePrefix, Pageable pageable);
    
    /**
     * 根据员工编号和日期前缀查询记录
     */
    List<Sign> findByNumberAndDateOnly(int number, String dateOnly);
    
    /**
     * 查询所有记录（不分页，用于统计）
     */
    List<Sign> findAllByOrderByTimestampDesc();
    
    /**
     * 根据日期查询所有记录
     */
    List<Sign> findByDateOnly(String dateOnly);

    List<Sign> findByDateOnlyAndState(String dateOnly,String state);

    List<Sign> findByDateOnlyAndStateAndType(String dateOnly,String state,String type);

    /**
     * 统计指定日期前缀和状态的记录数
     */
    long countByDateOnlyAndState(String dateOnly, String state);

    /**
     * 统计指定日期、状态和类型的记录数
     */
    long countByDateOnlyAndStateAndType(String dateOnly, String state, String type);

    /**
     * 根据日期前缀和状态查询记录（分页）
     */
    Page<Sign> findByDateOnlyAndState(String dateOnly, String state, Pageable pageable);

    Page<Sign> findByDateOnlyAndStateAndType(String dateOnly, String state, String type, Pageable pageable);
    long deleteByDateOnly(String dateOnly);

    default void approveMakeUp(String id) {
        Sign sign = findById(id).orElse(null);
        if (sign != null) {
            sign.setSign_address("福建省福州市马尾区新大陆科技有限公司");
            sign.setState("已签到");
            save(sign);
        }
    }
    default void rejectMakeUp(String id) {
        Sign sign = findById(id).orElse(null);
        if (sign != null) {
            sign.setState("未签到");
            save(sign);
        }
    }
} 