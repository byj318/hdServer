package com.huamai.hdServer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Auther: 旭燃
 * @Date: 2019/9/17 09:43
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TableVO implements Serializable {
    private String data;//数据
    private String title;//表头
}
