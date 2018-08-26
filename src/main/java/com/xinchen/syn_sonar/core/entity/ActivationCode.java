package com.xinchen.syn_sonar.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 允许执行自动同步的邀请码
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 18:19
 */
@Entity
@Data
@Proxy(lazy = false)
public class ActivationCode implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JSONField(ordinal = 1)
    private Long id;

    @Column(name = "code",nullable = false)
    @JSONField(ordinal = 2)
    private String code;
}
