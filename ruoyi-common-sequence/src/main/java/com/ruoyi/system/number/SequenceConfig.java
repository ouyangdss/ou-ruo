package com.ruoyi.system.number;


import lombok.Data;

/**
 * @author dss
 * @version 1.0.0
 * @description 数据库数字序列号配置
 * @className SequenceConfig.java
 * @createTime 2020年12月16日 10:00:00
 */
@Data
public class SequenceConfig {
    /** 序列号唯一标识*/
    private String id;
    /** 当前序列号*/
    private long current;
    /** 最大可用序列号*/
    private long maximum;

    public SequenceConfig() {
        super();
    }

    public SequenceConfig(String id) {
        super();
        this.id = id;
    }
}
