package com.huamai.hdServer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/1 16:32
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ScreenVO implements Serializable {
    private String banci;
    private String bedcode;
    private String name;
    private String shangjishijian;
    private String xiajishijian;
    private String state;
    private String dialysisType;
}
