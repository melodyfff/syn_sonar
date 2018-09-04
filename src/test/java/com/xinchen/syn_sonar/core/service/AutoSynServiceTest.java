package com.xinchen.syn_sonar.core.service;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 7:06
 */
public class AutoSynServiceTest extends SynSonarApplicationTests {

    @Resource
    private AutoSynService autoSynService;

    @Test
    public void test(){
        autoSynService.synchronize(true);
    }
}