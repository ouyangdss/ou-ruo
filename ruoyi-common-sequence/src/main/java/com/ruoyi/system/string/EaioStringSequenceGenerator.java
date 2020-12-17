package com.ruoyi.system.string;

import com.eaio.uuid.UUIDGen;
import com.ruoyi.system.StringSequenceGenerator;

import java.util.UUID;

/**
 * @author dss
 * @version 1.0.0
 * @description UUID产生唯一的序列号生成器
 * @className JvmStringSequenceGenerator.java
 * @createTime 2020年12月17日 09:17:00
 */
public class EaioStringSequenceGenerator implements StringSequenceGenerator {

    @Override
    public String getStringSequence() {
        return new UUID(UUIDGen.newTime(),UUIDGen.getClockSeqAndNode()).toString();
    }
}
