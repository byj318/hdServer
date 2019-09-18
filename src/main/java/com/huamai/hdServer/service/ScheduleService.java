package com.huamai.hdServer.service;

import com.huamai.hdServer.vo.OutDataVO;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/9 16:43
 * @Description:
 */
public interface ScheduleService {
    void getData(Long hospitalId);
    OutDataVO getDataAjax(Long hospitalId);
}
