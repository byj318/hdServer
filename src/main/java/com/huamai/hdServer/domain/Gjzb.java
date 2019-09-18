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
public class Gjzb implements Serializable {
    private String patientId;
    private Double val;
    private String value;
}
