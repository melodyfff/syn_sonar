package com.xinchen.syn_sonar.core.service;

import com.xinchen.syn_sonar.core.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 22:41
 */
public interface UserQueryService {
    /**
     * 查询所有用户
     * @param qPageRequest 查询条件
     * @return RulePage<User>
     */
    Page<User> findUsers(PageRequest qPageRequest);
}
