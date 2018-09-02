package com.xinchen.syn_sonar.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/2 19:43
 */
@Entity
@Data
@Proxy(lazy = false)
public class AutoSynResult implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JSONField(ordinal = 1)
    private Long id;

    /** 规则名 ("name" -> "Security - Array is stored directly")**/
    @Column(name = "rule_name",nullable = false)
    @JSONField(ordinal = 2)
    private String ruleName;

    /** 规则KEY ("key" -> "pmd:ArrayIsStoredDirectly")**/
    @Column(name = "rule_key",nullable = false)
    @JSONField(ordinal = 3)
    private String ruleKey;

    /** 严重程度 （Blocker | Critical | Major | Minor | Info）**/
    @Column(name = "severity",nullable = false)
    @JSONField(ordinal = 4)
    private String severity;

    /** 创建时间 **/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    @JSONField(ordinal = 5,format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    /** 是否是多出来的 （TRUE:local比remote多的   FALSE:remote比local少的） **/
    @Column(name = "is_more")
    @JSONField(ordinal = 6)
    private boolean isMore;

}
