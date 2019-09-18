package com.huamai.hdServer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class GjzbPatientTj implements Serializable {
    private String patientId;
    private String name;//病人姓名
    private String casecode;//病历本号
    private String identitycard;//身份证
    private String firstdialysetime;//首次透析日期
    private String sfrq;//检查日期
    private String val;//检查结果
    private String hospitalId;//院区
    private Integer number;
    public GjzbPatientTj(String name,String casecode,String identitycard,String firstdialysetime,String val,String hospitalId){
        this.name = name;
        this.casecode = casecode;
        this.identitycard = identitycard;
        this.firstdialysetime = firstdialysetime;
        this.val = val;
        this.hospitalId = hospitalId;
    }
}
