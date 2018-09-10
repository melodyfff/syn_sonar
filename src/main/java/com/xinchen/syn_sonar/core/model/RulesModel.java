package com.xinchen.syn_sonar.core.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 3:50
 */
@Data
public class RulesModel implements Serializable {
    /** 规则名 ("name" -> "Security - Array is stored directly")**/
    private String name;

    /** 所属语言的KEY值 **/
    private String profileKey;

    /** 查询关键KEY ("key" -> "pmd:ArrayIsStoredDirectly")**/
    private String key;

    /** 严重程度 （Blocker | Critical | Major | Minor | Info）**/
    private String severity;

    /** 当前状态 （CODE_SMELL | READY）**/
    private String type;

    /** 当前状态 （"status" -> "DEPRECATED"）**/
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        RulesModel that = (RulesModel) o;
        return (Objects.equals(name, that.name) &&
                Objects.equals(severity, that.severity))|(Objects.equals(key, that.key) &&
                Objects.equals(severity, that.severity));
    }

    @Override
    public int hashCode() {
        return Objects.hash( key, severity);
    }
}
