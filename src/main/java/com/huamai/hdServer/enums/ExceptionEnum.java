package com.huamai.hdServer.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @author bbd
 * @date 2018/9/15
 */
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    YBJCMPSDJL_CREATE_FAILED(500, "新增仪表及触摸屏设定记录失败"),
    YBJCMPSDJL_UPDATE_FAILED(500, "修改仪表及触摸屏设定记录失败"),
    YBJCMPSDJL_DELETE_FAILED(500, "删除仪表及触摸屏设定记录失败"),
    ANSYPZJL_CREATE_FAILED(500, "新增A浓缩液配置记录失败"),
    ANSYPZJL_UPDATE_FAILED(500, "修改A浓缩液配置记录失败"),
    ANSYPZJL_DELETE_FAILED(500, "删除A浓缩液配置记录失败");
    int value;
    String message;

    public int value() {
        return this.value;
    }

    public String message() {
        return this.message;
    }


}
