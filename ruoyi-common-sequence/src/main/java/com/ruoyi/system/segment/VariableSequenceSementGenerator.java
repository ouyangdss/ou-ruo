package com.ruoyi.system.segment;

import com.ruoyi.system.exception.SequenceException;
import com.ruoyi.system.service.VariableSementGenerator;
import jdk.internal.org.objectweb.asm.tree.analysis.Value;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dss
 * @version 1.0.0
 * @description 采用变量匹配模板的序列号片段生成器
 * @className VariableSequenceSementGenerator.java
 * @createTime 2020年12月17日 11:29:00
 */
public class VariableSequenceSementGenerator implements VariableSementGenerator, InitializingBean {
    /** 变量匹配模板*/
    private String pattern;
    /** 追加常量值*/
    private List<String> appendList = new ArrayList<>();
    /** 追加变量值*/
    private List<String> patternList = new ArrayList<>();
    final Pattern p = Pattern.compile("\\$\\{\\w+\\.?\\w+\\}");

    @Override
    public String getStringSequence(Map<String, String> argsMap) {
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<appendList.size();i++){
            sb.append(appendList.get(i));
            if(i<patternList.size()){
                String append = argsMap.get(patternList.get(i));
                if(StringUtils.isBlank(append)){
                    throw new SequenceException("this is no variable key "+patternList.get(i)+" in args map "+argsMap);
                }
                sb.append(append);
            }
        }
        return sb.toString();
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        Matcher matcher = p.matcher(pattern);
        int start = 0,end = 0;
        while (matcher.find()){
            String arg = matcher.group();
            start = matcher.start();
            String append = pattern.substring(end,start);
            end = matcher.end();
            appendList.add(append);
            patternList.add(arg.substring(2,arg.length()-1));
        }
        if(end != pattern.length()){
            appendList.add(pattern.substring(end));
        }
    }
    @Override
    public String getStringSegment() {
        throw new SequenceException("VariableSequenceSementGenerator should use getStringSegment with variable args");
    }


}
