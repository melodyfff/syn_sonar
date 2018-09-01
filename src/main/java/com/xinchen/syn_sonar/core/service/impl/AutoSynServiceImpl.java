package com.xinchen.syn_sonar.core.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.xinchen.syn_sonar.core.entity.BaseProfile;
import com.xinchen.syn_sonar.core.model.ProfilesModel;
import com.xinchen.syn_sonar.core.model.RulesModel;
import com.xinchen.syn_sonar.core.repository.BaseProfileRepository;
import com.xinchen.syn_sonar.core.service.AutoSynService;
import com.xinchen.syn_sonar.core.service.SonarService;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 6:23
 */
@Service
public class AutoSynServiceImpl implements AutoSynService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SonarService sonarService;

    @Autowired
    private BaseProfileRepository baseProfileRepository;

    @Override
    public void synchronize(boolean synchronize) {
        try {

            // 获取本地
            final Map<String, String> localMap = sonarService.searchDefaultFileLocal();
            // 获取远程
            final Map<String, String> remoteMap = sonarService.searchDefaultFileRemote();

            final List<BaseProfile> all = baseProfileRepository.findAll();
            all.forEach((x) -> {
                try {
                    List<RulesModel> localList = sonarService.searchRulesByFileNameLocal(localMap.get(x.getProfiles()));
                    List<RulesModel> remoteList = sonarService.searchRulesByFileNameRemote(remoteMap.get(x.getProfiles()));

                    // 比较本地和远程（本地有而远程没有的）
                    final List<RulesModel> more = sonarService.compareRule(localList, remoteList);
                    LOGGER.info("检测语言 [{}]  - 本地有而远程没有的数目为 [{}] , 具体为：{}", x.getProfiles(), more.size(), JSONObject.toJSON(more));
                    // 比较远程和本地（远程和本地存在差异的）
                    final List<RulesModel> diff = sonarService.compareRule(remoteList, localList);
                    // 此处对比完差异化，profileskey值为远程服务器中的，需要替换为本地环境中的，不然修改不生效
                    diff.forEach((y)->y.setProfileKey(localList.get(0).getProfileKey()));

                    LOGGER.info("检测语言 [{}]  -差异规则数目为 [{}] , 具体为：{}", x.getProfiles(), diff.size(), JSONObject.toJSON(diff));
                    if (synchronize) {
                        // 将和远程差异的部分进行同步，多出来的部分进行禁止
                        sonarService.updateRules(diff, more);
                    }

                } catch (IOException | AuthenticationException e) {
                    e.printStackTrace();
                    LOGGER.error("检测失败：{}", e.getMessage());
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("检测失败：{}", e.getMessage());
        }
    }

}
