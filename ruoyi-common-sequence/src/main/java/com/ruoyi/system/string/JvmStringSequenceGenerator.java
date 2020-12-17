package com.ruoyi.system.string;

import com.ruoyi.system.StringSequenceGenerator;

import java.util.UUID;

/**
 * @author dss
 * @version 1.0.0
 * @description 直接使用java自带的UUID产生唯一的序列号生成器
 * @className JvmStringSequenceGenerator.java
 * @createTime 2020年12月17日 09:17:00
 */
public class JvmStringSequenceGenerator implements StringSequenceGenerator {

    @Override
    public String getStringSequence() {
        return UUID.randomUUID().toString();
    }
}
