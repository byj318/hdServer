package com.huamai.hdServer.enums;

/**
 * @Auther: byj
 * @Date: 2019/3/14 10:42
 * @Description:  院区
 */
public enum HospitalEnum {
    NEW_DISTRICT(1), //新院区
    OLD_DISTRICT(2),//老院区
    FULL_DISTRICT(3);//全院

    private Integer value;

    HospitalEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
