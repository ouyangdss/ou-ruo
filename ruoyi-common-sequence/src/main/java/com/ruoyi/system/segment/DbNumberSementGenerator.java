package com.ruoyi.system.segment;

import com.ruoyi.system.number.DbNumberSequenceGenerator;
import com.ruoyi.system.service.SegmentGenerator;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className DbNumberSementGenerator.java
 * @createTime 2020年12月17日 14:07:00
 */
public class DbNumberSementGenerator extends DbNumberSequenceGenerator implements SegmentGenerator {
    /** 生成序列号的长度*/
    private int length;
    /** 是否左侧补零*/
    private boolean leftZeroPadding = true;

    @Override
    public String getStringSegment() {
        long numbericSequence = getNumberSequence();
        String originalSequence = String.valueOf(numbericSequence);
        String sementString = originalSequence;
        if(originalSequence.length() < length && leftZeroPadding){
            int subSeqLength = length - originalSequence.length();
            StringBuffer zeroBuffer = new StringBuffer();
            for(int i =0;i<subSeqLength;i++){
                zeroBuffer.append("0");
            }
            sementString = zeroBuffer.toString().concat(originalSequence);
        }
        return sementString;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setLeftZeroPadding(boolean leftZeroPadding) {
        this.leftZeroPadding = leftZeroPadding;
    }
}
