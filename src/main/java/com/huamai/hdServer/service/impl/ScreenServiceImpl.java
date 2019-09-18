package com.huamai.hdServer.service.impl;

import com.huamai.hdServer.domain.Screen;
import com.huamai.hdServer.mapper.ScreenMapper;
import com.huamai.hdServer.service.ScreenService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/2 10:13
 * @Description:
 */
@Service
public class ScreenServiceImpl implements ScreenService {
    @Autowired
    private ScreenMapper screenMapper;
    @Override
    public List<List<Screen>> listAll() {
        Screen screen = new Screen();
        screen.setIsDeleted(false);
        List<Screen> screenList = screenMapper.select(screen);
        return ListUtils.partition(screenList, 13);
    }
}
