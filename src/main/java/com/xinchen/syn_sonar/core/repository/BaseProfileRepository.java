package com.xinchen.syn_sonar.core.repository;

import com.xinchen.syn_sonar.core.entity.BaseProfile;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 15:55
 */
public interface BaseProfileRepository extends JpaRepository<BaseProfile, Long> {
}
