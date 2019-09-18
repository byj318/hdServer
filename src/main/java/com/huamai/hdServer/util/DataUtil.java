package com.huamai.hdServer.util;

import org.springframework.util.StringUtils;

import java.text.NumberFormat;

public class DataUtil {
    /**
     * 保留两位小数
     * @param d
     * @return
     */
    public static String formatDouble(double d) {
        if(StringUtils.isEmpty(d)) {
            return "0";
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(d);
    }

    /**
     * 百分数保留两位小数
     * @param d
     * @return
     */
    public static String formatPercentage(double d) {
        if(StringUtils.isEmpty(d)) {
            return "0";
        }
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        return nf.format(d);
    }
}
