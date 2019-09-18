package com.huamai.hdServer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Screen implements Serializable {
    private String name;
    private String dialysisType;
    private String bedNum;
    private String state;
    private String time;
    @JsonIgnore
    private Boolean isDeleted;
}
