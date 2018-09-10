package com.xinchen.syn_sonar.web;

import com.google.common.collect.Maps;
import com.xinchen.syn_sonar.core.entity.AutoSynLog;
import com.xinchen.syn_sonar.core.entity.AutoSynResult;
import com.xinchen.syn_sonar.core.entity.BaseProfile;
import com.xinchen.syn_sonar.core.repository.AutoSynLogRepository;
import com.xinchen.syn_sonar.core.repository.AutoSynResultRepository;
import com.xinchen.syn_sonar.core.repository.BaseProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Resource
    private BaseProfileRepository baseProfileRepository;


    @RequestMapping(value = "latest",method = RequestMethod.POST)
    public ModelAndView latest(@RequestParam(name = "page",defaultValue = "0") Integer page,
                               @RequestParam(name = "size",defaultValue = "1") Integer size){
        // 查询最新更新
        final Page<AutoSynLog> autoSynLogs = autoSynLogRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC,"createDate"));
        // 根据数据库中需要同步的数据进行展示
        final List<BaseProfile> baseProfiles = baseProfileRepository.findAll();

        Map<String, List<AutoSynResult>> map = Maps.newHashMap();
        baseProfiles.forEach((x)->{
            map.put(x.getProfiles(), new ArrayList<AutoSynResult>());
        });

        ModelAndView mav = new ModelAndView("page/latest");

        autoSynLogs.forEach((x)->{
            final List<AutoSynResult> autoSynResults = autoSynResultRepository.queryAutoSynResult(x.getCreateDate());
            map.putAll(autoSynResults.stream().collect(Collectors.groupingBy(AutoSynResult::getLanguage)));
            mav.addObject("time", x.getCreateDate());
        });

        mav.addObject("maps", map);
        return mav;
    }


    @RequestMapping(value = "history")
    public ModelAndView results(@RequestParam(name = "page",defaultValue = "0") Integer page,
                                @RequestParam(name = "size",defaultValue = "10") Integer size){
        final Page<AutoSynResult> autoSynLogs = autoSynResultRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC,"createDate"));


        ModelAndView mav = new ModelAndView("page/history");
        mav.addObject("datas", autoSynLogs);
        return mav;
    }
}
