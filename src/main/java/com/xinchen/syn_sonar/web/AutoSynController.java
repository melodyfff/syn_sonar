package com.xinchen.syn_sonar.web;

import com.xinchen.syn_sonar.core.entity.ActivationCode;
import com.xinchen.syn_sonar.core.repository.ActivationCodeRepository;
import com.xinchen.syn_sonar.core.service.AutoSynService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 16:23
 */
@RestController
public class AutoSynController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ActivationCodeRepository codeRepository;

    @Autowired
    private AutoSynService autoSynService;


    @PostMapping(value = "/autoSyn")
    public ResponseEntity autoSyn(HttpServletRequest request,
                                  @RequestParam(name = "code", defaultValue = "") String code) throws Exception {

        LOGGER.info(">>>>>>>>触发一键同步");
        final List<ActivationCode> all = codeRepository.findAll();
        if (all.stream().anyMatch((x)->x.getCode().equals(code.trim()))){

            // 执行同步

            autoSynService.synchronize(true);

            LOGGER.info(">>>>>>>>一键同步完成");
           return new ResponseEntity(HttpStatus.OK);
        } else {
            LOGGER.error(">>>>>>>>执行码错误");
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
