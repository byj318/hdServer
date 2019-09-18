package com.huamai.hdServer.vo;

import com.huamai.hdServer.domain.ScreenVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/10 17:48
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class OutDataVO implements Serializable {
    private String riqi;
    private String banci;
    private String notice;
    private List<List<ScreenVO>> data;
}
