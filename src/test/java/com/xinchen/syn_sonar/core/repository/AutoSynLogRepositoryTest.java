package com.xinchen.syn_sonar.core.repository;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import com.xinchen.syn_sonar.core.entity.AutoSynLog;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/2 23:06
 */
public class AutoSynLogRepositoryTest extends SynSonarApplicationTests {

    @Autowired
    private AutoSynLogRepository autoSynLogRepository;

    @Autowired
    private AutoSynResultRepository autoSynResultRepository;

    @Test
    public void queryAll() {
        final Iterable<AutoSynLog> all = autoSynLogRepository.findAll();

        all.forEach((x)->{
            System.out.println(autoSynResultRepository.queryAutoSynResult(x.getCreateDate()));
        });


    }
}