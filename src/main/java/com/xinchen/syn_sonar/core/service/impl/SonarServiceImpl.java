package com.xinchen.syn_sonar.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.xinchen.syn_sonar.core.api.AdressComponent;
import com.xinchen.syn_sonar.core.api.ApiContants;
import com.xinchen.syn_sonar.core.entity.BaseProfile;
import com.xinchen.syn_sonar.core.model.ProfilesModel;
import com.xinchen.syn_sonar.core.model.RulesModel;
import com.xinchen.syn_sonar.core.repository.BaseProfileRepository;
import com.xinchen.syn_sonar.core.service.SonarService;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Sonar服务调用接口
 *
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 1:40
 */
@Service
public class SonarServiceImpl implements SonarService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AdressComponent adressComponent;

    @Autowired
    private BaseProfileRepository baseProfileRepository;


    @Override
    public Map<String,String> searchDefaultFileLocal() throws IOException {
        return searchDefaultFile(adressComponent.getLocalUrl());
    }

    @Override
    public Map<String,String> searchDefaultFileRemote() throws IOException {
        return searchDefaultFile(adressComponent.getRemoteUrl());
    }

    @Override
    public Map<String,String> searchDefaultFile(String domain) throws IOException {
        // 构建查询条件
        List<NameValuePair> params = Lists.newArrayList();
        // 查询所有为defaults的
        params.add(new BasicNameValuePair("defaults", "true"));

        // 调用API查询
        HttpEntity httpEntity = doExecuteGet(domain, ApiContants.API_SEARCH_DEFAULTFILE, params);

        // 返回结果处理
        final Map parse = (Map) JSONObject.parse(EntityUtils.toString(httpEntity, "UTF-8"));
        final List<JSONObject> profiles = (List<JSONObject>) parse.get("profiles");

        LOGGER.info("查询 [{}] Profiles 为 defaults 结束", domain);

        // 由数据库中的字段控制同步检测那些语言，以及同步哪些语言
        final List<BaseProfile> profile = baseProfileRepository.findAll();

        List<ProfilesModel> list = JSON.parseObject(profiles.toString(), new TypeReference<List<ProfilesModel>>(){});

        return  list.stream()
                .filter(x -> profile.stream().anyMatch((y)->y.getProfiles().equals(x.getLanguageName())))
                .collect(Collectors.toMap(ProfilesModel::getLanguageName,ProfilesModel::getKey));
    }

    @Override
    public List<RulesModel> searchRulesByFileNameLocal(String key) throws IOException {
        return searchRulesByFileName(adressComponent.getLocalUrl(), key);
    }

    @Override
    public List<RulesModel> searchRulesByFileNameRemote(String key) throws IOException {
        return searchRulesByFileName(adressComponent.getRemoteUrl(), key);
    }


    @Override
    public List<RulesModel> searchRulesByFileName(String domain, String key) throws IOException {

        boolean flag = true;
        // 查询第几页 （sonar API查询默认为一页最多500）
        int page = 1;

        List<RulesModel> resultList = Lists.newArrayList();

        while (flag) {
            // 构建查询条件
            List<NameValuePair> params = Lists.newArrayList();
            // 查询所有为defaults的
            params.add(new BasicNameValuePair("ps", "500"));
            params.add(new BasicNameValuePair("activation", "true"));
            params.add(new BasicNameValuePair("qprofile", key));
            params.add(new BasicNameValuePair("p", Objects.toString(page)));
            // 调用API查询
            HttpEntity httpEntity = doExecuteGet(domain, ApiContants.API_SEARCH_RULE, params);
            // 返回结果处理
            Map parse = (Map) JSONObject.parse(EntityUtils.toString(httpEntity, "UTF-8"));
            List<RulesModel> rules = (List<RulesModel>) parse.get("rules");
            if (rules.isEmpty()) {
                flag = false;
            } else {
                resultList.addAll(JSON.parseObject(rules.toString(), new TypeReference<List<RulesModel>>() {
                }));
                page += 1;
            }
        }
        LOGGER.info("获取 key为 [{}] 的规则结束 ,共 [{}] ,条", key, resultList.size());
        // 设置语言的查找ProfileKey值
        resultList.forEach((x) -> x.setProfileKey(key));
        return resultList;
    }

    @Override
    public List<RulesModel> compareRule(List<RulesModel> list1, List<RulesModel> list2) {

        // 求交集
        List<RulesModel> intersection = list1.stream().filter(list2::contains).collect(Collectors.toList());

        // 求差集 remote - local
        List<RulesModel> reduce = list1.stream().filter((x) -> !list2.contains(x)).collect(Collectors.toList());

        return reduce;
    }

    @Override
    public void updateRules(List<RulesModel> active, List<RulesModel> deactivate) throws IOException, AuthenticationException {
        LOGGER.info("开始同步...");
        LOGGER.info("同步地址为: {}",adressComponent.getLocalUrl());

        // 激活
        if (null != active && !active.isEmpty()) {
            active.forEach((x) -> {
                List<NameValuePair> params = Lists.newArrayList();
                params.add(new BasicNameValuePair("profile_key", x.getProfileKey()));
                params.add(new BasicNameValuePair("rule_key", x.getKey()));
                params.add(new BasicNameValuePair("severity", x.getSeverity()));
                try {
                    doExecutePost(adressComponent.getLocalUrl(), ApiContants.API_ACTIVATE_RULE, params);
                    LOGGER.info("激活/同步规则成功 , [{}] - key:[{}] ... ", x.getName(), x.getKey());
                } catch (IOException | AuthenticationException e) {
                    LOGGER.error("同步失败！, [{}] - key:[{}] ... ", x.getName(), x.getKey());
                }
            });
        }

        // 禁止
        if (null != deactivate && !deactivate.isEmpty()) {
            deactivate.forEach((x)->{
                List<NameValuePair> params = Lists.newArrayList();
                params.add(new BasicNameValuePair("profile_key", x.getProfileKey()));
                params.add(new BasicNameValuePair("rule_key", x.getKey()));
                try {
                    doExecutePost(adressComponent.getLocalUrl(), ApiContants.API_DEACTIVATE_RULE, params);
                    LOGGER.info("禁止规则成功 , [{}] - key:[{}] ... ", x.getName(), x.getKey());
                } catch (IOException | AuthenticationException e) {
                    LOGGER.error("禁止失败！, [{}] - key:[{}] ... ", x.getName(), x.getKey());
                }
            });
        }
        LOGGER.info("同步完成...");
    }


    private HttpEntity doExecuteGet(String url, String api, List<NameValuePair> params) throws IOException {
        final CloseableHttpClient client = HttpClients.createDefault();
        final CloseableHttpResponse response = client.execute(httpGetMaker(url, api, params));
        return response.getEntity();
    }

    private HttpGet httpGetMaker(String url, String api, List<NameValuePair> params) throws IOException {
        HttpGet httpGet = new HttpGet(this.requestMaker(url, api, params));
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(6000)
                .setConnectTimeout(6000)
                .setConnectionRequestTimeout(6000).build();
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    private HttpEntity doExecutePost(String url, String api, List<NameValuePair> params) throws IOException, AuthenticationException {
        final CloseableHttpClient client = HttpClients.createDefault();
        final CloseableHttpResponse response = client.execute(httpPostMaker(url, api, params));
        return response.getEntity();
    }

    private HttpPost httpPostMaker(String url, String api, List<NameValuePair> params) throws IOException, AuthenticationException {
        HttpPost httpPost = new HttpPost(this.requestMaker(url, api, null));
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(6000)
                .setConnectTimeout(6000)
                .setConnectionRequestTimeout(6000).build();
        httpPost.setConfig(requestConfig);
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(adressComponent.getLocalUsername(), adressComponent.getLocalPassword());
        httpPost.addHeader(new BasicScheme().authenticate(credentials, httpPost, null));
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        return httpPost;
    }

    private String requestMaker(String url, String api, List<NameValuePair> params) throws IOException {
        if (null == params || params.isEmpty()) {
            return url + api;
        }
        return url + api + "?" + EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
    }
}
