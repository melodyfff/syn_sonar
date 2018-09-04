package com.xinchen.syn_sonar.core.service;

import com.xinchen.syn_sonar.core.model.ProfilesModel;
import com.xinchen.syn_sonar.core.model.RulesModel;
import org.apache.http.auth.AuthenticationException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 1:39
 */
public interface SonarService {

    /**
     * 查询本地为 default 的Profiles
     * @return Map<String,String>
     * @throws IOException IOException
     */
    Map<String,String> searchDefaultFileLocal() throws IOException;

    /**
     * 查询远程为 default 的Profiles
     * @return Map<String,String>
     * @throws IOException IOException
     */
    Map<String,String> searchDefaultFileRemote() throws IOException;

    /**
     * 查询自定义地址为 default 的Profiles
     * @param domain 查询地址
     * @return Map<String,String>
     * @throws IOException IOException
     */
    Map<String,String> searchDefaultFile(String domain) throws IOException;

    /**
     * 根据rule的key，查询本地的激活规则
     * @param key 规则key值
     * @return List<RulesModel>
     * @throws IOException IOException
     */
    List<RulesModel> searchRulesByFileNameLocal(String key) throws IOException;

    /**
     * 根据rule的key，查询远程的激活规则
     * @param key 规则key值
     * @return List<RulesModel>
     * @throws IOException IOException
     */
    List<RulesModel> searchRulesByFileNameRemote(String key) throws IOException;

    /**
     * 根据rule的key，查询自定义地址的激活规则
     * @param key 规则key值
     * @return List<RulesModel>
     * @throws IOException IOException
     */
    List<RulesModel> searchRulesByFileName(String domain, String key) throws IOException;


    /**
     * 比较远程和本地的规则差异
     * @param list1 规则合集
     * @param list2 远程规则合集
     * @return 差集 (remote - local)
     */
    List<RulesModel> compareRule(List<RulesModel> list1,List<RulesModel> list2);

    /**
     * 进行激活或者禁止规则
     * @param active 激活规则合集
     * @param deactivate 禁止规则合集
     */
    void updateRules(List<RulesModel> active,List<RulesModel> deactivate) throws IOException, AuthenticationException;

}
