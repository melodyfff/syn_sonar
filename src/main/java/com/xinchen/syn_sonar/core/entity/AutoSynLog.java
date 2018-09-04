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
 * @date Created In 2018/9/2 22:08
 */
@Entity
@Data
@Proxy(lazy = false)
public class AutoSynLog implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JSONField(ordinal = 1)
    private Long id;

    /** 创建时间 **/
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    @JSONField(ordinal = 5,format="yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    public AutoSynLog(Date createDate) {
        this.createDate = createDate;
    }
}
