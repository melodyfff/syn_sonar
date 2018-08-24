package com.xinchen.syn_sonar;

import com.xinchen.syn_sonar.core.entity.User;
import com.xinchen.syn_sonar.core.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SynSonarApplicationTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testIn(){
        User user = new User();
        user.setUsername("test");
        user.setPassword("ok");
        user.setCreateDate(new Date());
        userRepository.save(user);

    }


}
