package com.xinchen.syn_sonar.core.service;

/**
 * 自动同步接口
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 6:22
 */
public interface AutoSynService {

    /**
     * 对比差异,自动同步
     * @param synchronize 是否对比完自动同步
     */
    void synchronize(boolean synchronize);


}
