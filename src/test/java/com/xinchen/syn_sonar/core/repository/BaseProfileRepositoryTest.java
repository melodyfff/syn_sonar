package com.xinchen.syn_sonar.core.repository;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import com.xinchen.syn_sonar.core.entity.BaseProfile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 15:56
 */
public class BaseProfileRepositoryTest extends SynSonarApplicationTests {
    @Autowired
    private BaseProfileRepository baseProfileRepository;

    @Test
    public void testBase(){
        BaseProfile baseProfile = new BaseProfile();
        baseProfile.setProfiles("java");
        baseProfileRepository.save(baseProfile);
    }

}