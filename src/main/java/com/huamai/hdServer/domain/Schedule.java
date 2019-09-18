package com.huamai.hdServer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.util.Date;

/**
 * @Auther: 旭燃
 * @Date: 2019/5/9 16:14
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Schedule {
    private Long id;
    @Column(name = "StartTime" )
    private Date startTime;
    private Integer frequence;
    @Column(name = "DailyNum")
    private Integer dailyNum;
    @Column(name = "UserID")
    private Integer userId;
    @Column(name = "CurrentMark")
    private Integer currentMark;

    private Date createTime;
    private Date modifyTime;
    private Date deleteTime;
    private Boolean delMark;
    private Long hospitalId;
}
