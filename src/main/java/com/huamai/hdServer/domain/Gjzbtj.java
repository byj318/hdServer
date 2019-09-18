package com.huamai.hdServer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: byj
 * @Date: 2019/3/13 15:38
 * @Description:  关键指标统计  实体类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Gjzbtj implements Serializable{
    private String a1;
    private String a2;
    private String a3;
    private String bfb;//百分比
}
