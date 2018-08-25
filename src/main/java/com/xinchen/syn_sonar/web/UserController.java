package com.xinchen.syn_sonar.web;

import com.xinchen.syn_sonar.core.entity.User;
import com.xinchen.syn_sonar.core.service.UserQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 23:27
 */
@Controller
public class UserController {

    @Autowired
    private UserQueryService userQueryService;

    @RequestMapping(value = "/getAllUser",method = RequestMethod.POST)
    public ModelAndView getAllUser(HttpServletRequest request,
                                   @RequestParam(name = "page",defaultValue = "0") Integer page,
                                   @RequestParam(name = "size",defaultValue = "5") Integer size){
        ModelAndView mav = new ModelAndView("page/user");
        final Page<User> users = userQueryService.findUsers(new QPageRequest(page, size));
        mav.addObject("datas", users);
        return mav;
    }

}
