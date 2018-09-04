package com.xinchen.syn_sonar.web;

import com.google.common.collect.Maps;
import com.xinchen.syn_sonar.core.entity.AutoSynLog;
import com.xinchen.syn_sonar.core.entity.AutoSynResult;
import com.xinchen.syn_sonar.core.repository.AutoSynLogRepository;
import com.xinchen.syn_sonar.core.repository.AutoSynResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/3 22:56
 */
@Controller
public class PageController {

    @Autowired
    private AutoSynResultRepository autoSynResultRepository;

    @Resource
    private AutoSynLogRepository autoSynLogRepository;


    @RequestMapping("")
    public ModelAndView results(@RequestParam(name = "page",defaultValue = "0") Integer page,
                                @RequestParam(name = "size",defaultValue = "3") Integer size){
        // 查询最近三次的
        final Page<AutoSynLog> autoSynLogs = autoSynLogRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC,"id"));

        Map<String, List<AutoSynResult>> map = Maps.newHashMap();

        autoSynLogs.forEach((x)->{
            final List<AutoSynResult> autoSynResults = autoSynResultRepository.queryAutoSynResult(x.getCreateDate());
            map.put(x.getCreateDate().toString(), autoSynResults);
        });

        ModelAndView mav = new ModelAndView("page/results");
        mav.addObject("datas", map);
        return mav;
    }
}
