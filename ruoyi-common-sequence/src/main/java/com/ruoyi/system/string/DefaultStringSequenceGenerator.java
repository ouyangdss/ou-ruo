package com.ruoyi.system.string;

import com.ruoyi.system.NumberSequenceGenerator;
import com.ruoyi.system.StringSequenceGenerator;
import org.springframework.util.Assert;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className DefaultStringSequenceGenerator.java
 * @createTime 2020年12月16日 20:58:00
 */
public class DefaultStringSequenceGenerator implements StringSequenceGenerator {

    public static final String EMPTY_STRING="";
    /** 数字序列号生成器*/
    private NumberSequenceGenerator numberSequenceGenerator;
    /** 是否左侧季补齐*/
    private boolean leftZeroPadding = true;
    /** 序列号长度(此处作为属性主要目的是左侧季补齐,请保证配置时数字序列号的位数不会超过此处设置的值)*/
    private int length = 8;
    /** 前缀*/
    private String prefix = "";

    @Override
    public String getStringSequence() {
        long numbericSequence = numberSequenceGenerator .getNumberSequence();
        StringBuilder builder = new StringBuilder();
        String stringSequence = String.valueOf(numbericSequence);
        builder.append(prefix);
        if (leftZeroPadding) {
            builder.append(getLeftZeroPadding(stringSequence));
        }
        builder.append(stringSequence);
        return builder.toString();
    }

    /**
     * 获取左侧零补齐
     * @param originalSequence
     * @return
     */
    private  String getLeftZeroPadding(String originalSequence) {
        int paddingLength=length- originalSequence.length();
        if (paddingLength >=0) {
            return paddingTable [paddingLength];
        }
        return EMPTY_STRING;
    }
    private final String[] paddingTable ={ "", "0", "00", "000", "0000", "00000", "000000", "0000000","00000000",
            "0000000000", "00000000000", "000000000000", "000000000000", "00000000000000", "0000000000000", "00000000000000",
            "000000000000000", "0000000000000000", "0000000000000000 ", "000000000000000000е" };

    public NumberSequenceGenerator getNumberSequenceGenerator() {
        return numberSequenceGenerator;
    }

    public void setNumberSequenceGenerator (NumberSequenceGenerator numberSequenceGenerator) {
        Assert.notNull(numberSequenceGenerator);
        this.numberSequenceGenerator = numberSequenceGenerator;
    }

    public boolean isLeftZeroPadding() {
        return leftZeroPadding;
    }
    public void setLeftZeroPadding(boolean leftZeroPadding) {
        this.leftZeroPadding = leftZeroPadding;
    }
    public int getLength() {
        return length;
    }
    public void setLength(int length) {
        Assert.isTrue(length > 0);
        this.length = length;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        Assert.hasText(prefix);
        this.prefix = prefix;
    }



    public static void main(String[] args) {
        int youNumber = 1;   //int num = String.valueOf(yourNum);
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        DefaultStringSequenceGenerator f = new DefaultStringSequenceGenerator();
        String str = String.format("%0"+f.length+"d", youNumber);
        System.out.println(str);

        System.out.println(f.getLeftZeroPadding("1"));
    }

}
