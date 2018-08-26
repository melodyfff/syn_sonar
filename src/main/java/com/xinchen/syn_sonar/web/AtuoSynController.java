package com.xinchen.syn_sonar.web;

import com.xinchen.syn_sonar.core.entity.ActivationCode;
import com.xinchen.syn_sonar.core.repository.ActivationCodeRepository;
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
public class AtuoSynController {

    @Autowired
    private ActivationCodeRepository codeRepository;


    @PostMapping(value = "/autoSyn")
    public ResponseEntity autoSyn(HttpServletRequest request,
                                  @RequestParam(name = "code", defaultValue = "") String code) {
        if (StringUtils.isEmpty(code)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
        final List<ActivationCode> all = codeRepository.findAll();

        return all.stream().anyMatch((x)->x.getCode().equals(code.trim())) ?
                new ResponseEntity(HttpStatus.OK) : new ResponseEntity(HttpStatus.FORBIDDEN);
    }
}
