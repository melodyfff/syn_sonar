package com.xinchen.syn_sonar.core.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
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
 * @date Created In 2018/8/25 1:17
 */
@Entity
@Data
@Proxy(lazy = false)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JSONField(ordinal = 1)
    private Long id;

    @Column(name = "user_name",nullable = false)
    @JSONField(ordinal = 2)
    private String username;

    @Column(name = "password",nullable = false)
    @JSONField(ordinal = 3)
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    @JSONField(ordinal = 4,format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}

