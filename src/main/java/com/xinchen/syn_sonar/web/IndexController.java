package com.xinchen.syn_sonar.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 17:14
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/login")
    public String returnToLogin(){
        return "/page/login";
    }

    @RequestMapping(value = "/403")
    public String returnTo403(){
        return "/error/403";
    }
}
