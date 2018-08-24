package com.xinchen.syn_sonar.core.repository;

import com.xinchen.syn_sonar.core.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 1:17
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
