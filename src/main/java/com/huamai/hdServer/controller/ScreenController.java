package com.huamai.hdServer.controller;

import com.huamai.hdServer.client.DemoWebSocketClient;
import com.huamai.hdServer.service.ScheduleService;
import com.huamai.hdServer.vo.OutDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

/**
 * @Auther: 旭燃
 * @Date: 2019/4/30 18:26
 * @Description: 大屏幕
 */
@Controller
@RequestMapping("screen")
public class ScreenController {

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private DemoWebSocketClient webSocketClient;
    @GetMapping
    public String index(){
        return "screen/screen";
    }

    @GetMapping("index/{hospitalId}")
    public String index2(@PathVariable(name = "hospitalId",required = true)Integer hospitalId, Model model){
        model.addAttribute("year", LocalDate.now().getYear());
        if(hospitalId == 1){
            return "screen/screen1";
        }else if(hospitalId == 2){
            return "screen/screen2";
        }
        return null;
    }

    @GetMapping("getData/{hospitalId}")
    @ResponseBody
    public OutDataVO getData(@PathVariable(name = "hospitalId",required = true)Long hospitalId){
        return scheduleService.getDataAjax(hospitalId);
    }

    /*@MessageMapping("/v1/chat/new")
    @Scheduled(fixedRate=600000)
    public void gameInfonew() {
        scheduleService.getData(1L);
    }

    @MessageMapping("/v1/chat/old")
    @Scheduled(fixedRate=600000)
    public void gameInfoOld() {
        scheduleService.getData(2L);
    }*/

}
