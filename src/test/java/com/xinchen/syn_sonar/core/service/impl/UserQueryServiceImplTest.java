package com.xinchen.syn_sonar.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xinchen.syn_sonar.core.entity.User;
import com.xinchen.syn_sonar.core.service.UserQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 23:17
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserQueryServiceImplTest {

    @Autowired
    private UserQueryService userQueryService;

    @Test
    public void testFind(){

        PageRequest qPageRequest = PageRequest.of(0,5);
        final Page<User> users = userQueryService.findUsers(qPageRequest);
        JSONObject.toJSON(users);

    }
}