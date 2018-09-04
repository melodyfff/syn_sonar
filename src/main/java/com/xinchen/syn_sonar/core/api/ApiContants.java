package com.xinchen.syn_sonar.core.api;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 1:45
 */
public final class ApiContants {

    /** sonar查询所有默认规则的api **/
    public final static String API_SEARCH_DEFAULTFILE ="/api/qualityprofiles/search";

    /** 通过默认文件的key，查询所有规则api **/
    public final static String API_SEARCH_RULE = "/api/rules/search";

    /** sonar激活规则api **/
    public final static String API_ACTIVATE_RULE = "/api/qualityprofiles/activate_rule";

    /** soanr废弃规则api **/
    public final static String API_DEACTIVATE_RULE = "/api/qualityprofiles/deactivate_rule";

}
