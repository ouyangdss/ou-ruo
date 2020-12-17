package com.ruoyi.system.service;

import java.util.Map;

/**
 * @author dss
 * @version 1.0.0
 * @description 含参数的流水号片段
 * @className VariableSementGenerator.java
 * @createTime 2020年12月17日 10:31:00
 */
public interface VariableSementGenerator  extends SegmentGenerator {
    /**
     * 含参数的流水号片段
     * @param atgsMap
     * @return
     */
    public String getStringSequence(Map<String,String> atgsMap);
}
