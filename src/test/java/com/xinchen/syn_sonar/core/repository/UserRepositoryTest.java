package com.xinchen.syn_sonar.core.repository;

import com.alibaba.fastjson.JSONObject;
import com.xinchen.syn_sonar.core.entity.User;
import com.xinchen.syn_sonar.core.service.UserQueryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 23:19
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testInsert() {
//        for (int i = 0; i < 100; i++) {
//            User user = new User();
//            user.setUsername("test");
//            user.setPassword("ok");
//            user.setCreateDate(new Date());
//            userRepository.save(user);
//        }
//        System.out.println(JSONObject.toJSON(userRepository.getOne(1l)));
    }
}