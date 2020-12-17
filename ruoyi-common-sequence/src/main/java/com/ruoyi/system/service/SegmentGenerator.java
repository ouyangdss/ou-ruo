package com.ruoyi.system.service;

/**
 * @author dss
 * @version 1.0.0
 * @description 流水号片段生成器
 * @className SegmentGenerator.java
 * @createTime 2020年12月17日 09:39:00
 */
public interface SegmentGenerator {
    /**
     * 获取流水号片段
     * @return
     */
    public String getStringSegment();
}
