package com.xinchen.syn_sonar.core.repository;

import com.xinchen.syn_sonar.core.entity.AutoSynResult;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/2 19:54
 */
public interface AutoSynResultRepository extends CrudRepository<AutoSynResult, Long> {
    /** 连表查询 **/
    @Query(value = "SELECT asr.id, asr.create_date, asr.is_more, asr.language, asr.rule_key, asr.rule_name, asr.severity from  auto_syn_result asr,auto_syn_log asl where asr.create_date = asl.create_date and asl.create_date = ?1 ORDER BY asr.create_date DESC",nativeQuery=true)
    List<AutoSynResult> queryAutoSynResult(@Param("qDate") Date qDate);
}
