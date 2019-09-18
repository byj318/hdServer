package com.huamai.hdServer.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Auther: byj
 * @Date: 2019/3/13 16:36
 * @Description:  关键指标统计  view object
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class GjzbtjVO {
    private String xgtlDbs;//血红蛋白达标数
    private String xgtlZs;//血红蛋白总数
    private String xgtlDbBfb;//血红蛋白达标百分比

    private String xhdbDbs;//血红蛋白达标数
    private String xhdbZs;//血红蛋白总数
    private String xhdbPjs;//血红蛋白平均数
    private String xhdbDbBfb;//血红蛋白达标百分比

    private String bdbDbs;//白蛋白达标数
    private String bdbZs;//白蛋白总数
    private String bdbPjs;//白蛋白平均数
    private String bdbDbBfb;//白蛋白达标百分比

    private String caDbs;//钙达标数
    private String caZs;//钙总数
    private String caPjs;//钙平均数
    private String caDbBfb;//钙达标百分比

    private String pDbs;//磷达标数
    private String pZs;//磷总数
    private String pPjs;//磷平均数
    private String pDbBfb;//磷达标百分比

    private String jzpxjsDbs;//甲状旁腺激素达标数
    private String jzpxjsZs;//甲状旁腺激素总数
    private String jzpxjsPjs;//甲状旁腺激素平均数
    private String jzpxjsDbBfb;//甲状旁腺激素达标百分比

    private String ktvDbs;//透析充分性达标数
    private String ktvZs;//透析充分性总数
    private String ktvPjs;//透析充分性平均数
    private String ktvDbBfb;//透析充分性达标百分比


}
