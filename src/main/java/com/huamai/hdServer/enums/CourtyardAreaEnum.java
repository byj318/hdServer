package com.huamai.hdServer.enums;

/**
 * @Auther: byj
 * @Date: 2019/3/14 14:18
 * @Description:  院区
 */
public enum CourtyardAreaEnum {
    NEW_DISTRICT_ZH_CN("新院"), //新院区
    OLD_DISTRICT_ZH_CN("老院"),//
    FULL_DISTRICT_ZH_CN("全院"),
    NEW_DISTRICT_EN_US("new"), //新院区
    OLD_DISTRICT_EN_US("old");//老院区
    private String value;

    CourtyardAreaEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
