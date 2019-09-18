package com.huamai.hdServer;

import com.huamai.hdServer.mapper.HistoryDialyseRecordMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HdServerApplicationTests {
    @Autowired
    private HistoryDialyseRecordMapper historyDialyseRecordMapper;

    @Test
    public void test() {

    }
}
