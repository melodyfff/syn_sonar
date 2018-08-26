package com.xinchen.syn_sonar.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 16:23
 */
@RestController
public class AtuoSynController {

    @PostMapping(value = "/autoSyn")
    public String autoSyn(HttpServletRequest request){
        return "ok";
    }
}
