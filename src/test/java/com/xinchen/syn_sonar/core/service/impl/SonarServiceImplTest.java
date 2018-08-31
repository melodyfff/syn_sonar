package com.xinchen.syn_sonar.core.service.impl;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import com.xinchen.syn_sonar.core.model.RulesModel;
import com.xinchen.syn_sonar.core.service.SonarService;
import org.apache.http.auth.AuthenticationException;
import org.assertj.core.util.Lists;
import org.junit.Test;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 2:25
 */
public class SonarServiceImplTest extends SynSonarApplicationTests {
    @Resource
    private SonarService sonarService;

    @Test
    public void searchDefaultFile() throws IOException {
        sonarService.searchDefaultFile("http://192.168.201.132:9001");
    }

    @Test
    public void searchRulesByfileName() throws IOException {
        sonarService.searchRulesByFileName("http://192.168.201.132:9001", "AWWAisuN_1_oTq4fsbh7");
    }

    @Test
    public void compareRule() throws IOException {
        List<RulesModel> local = Lists.newArrayList();
        RulesModel rulesModel1 = new RulesModel();
        rulesModel1.setName("Lines should not be too long");
        rulesModel1.setSeverity("MAJOR");
        RulesModel rulesModel2 = new RulesModel();
        rulesModel2.setName("Sections of code should not be \"commented out\"");
        rulesModel2.setSeverity("BLOCKER");
        local.add(rulesModel1);
//        local.add(rulesModel2);

        List<RulesModel> remote = Lists.newArrayList();
        RulesModel rulesModel3 = new RulesModel();
        rulesModel3.setName("Lines should not be too long");
        rulesModel3.setSeverity("MAJOR");
        RulesModel rulesModel4 = new RulesModel();
        rulesModel4.setName("Sections of code should not be \"commented out\"");
        rulesModel4.setSeverity("BLOCKER1");
        remote.add(rulesModel3);
        remote.add(rulesModel4);


        sonarService.compareRule(local, remote);

        sonarService.compareRule(remote, local);


    }

    @Test
    public void updateRules() throws IOException, AuthenticationException {
        sonarService.updateRules(null,null);
    }


}