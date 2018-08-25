package com.xinchen.syn_sonar.core.service.impl;

import com.xinchen.syn_sonar.core.entity.User;
import com.xinchen.syn_sonar.core.repository.UserRepository;
import com.xinchen.syn_sonar.core.service.UserQueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 23:03
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class UserQueryServiceImpl implements UserQueryService {

    @Resource
    private UserRepository userRepository;

    @Override
    public Page<User> findUsers(QPageRequest qPageRequest) {
        return userRepository.findAll(qPageRequest);
    }
}
