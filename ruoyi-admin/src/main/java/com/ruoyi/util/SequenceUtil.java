package com.ruoyi.util;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.string.DefaultStringSequenceGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className SequenceUtil.java
 * @createTime 2020年12月16日 21:31:00
 */
@Service
public class SequenceUtil {

    @Resource(name = "exchOrderIdSequenceGenerator")
     private DefaultStringSequenceGenerator exchOrderIdSequenceGenerator;

    public String getExchOrderIdSequence() {
        String str="E0" + DateUtils.dateTimeNow() + exchOrderIdSequenceGenerator.getStringSequence();
        return str;
    }
}
