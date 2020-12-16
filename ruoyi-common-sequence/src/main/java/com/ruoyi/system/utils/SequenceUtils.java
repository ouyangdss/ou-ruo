package com.ruoyi.system.utils;


import org.apache.commons.lang.math.RandomUtils;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author dss
 * @version 1.0.0
 * @description TODO
 * @className SequenceUtils.java
 * @createTime 2020年12月16日 11:21:00
 */
public class SequenceUtils {

    private static ZoneId ZONE_ID = ZoneId.of("Asia/Shanghai");
    private static final AtomicInteger SEQ = new AtomicInteger(100000);
    private static final DateTimeFormatter DF_FMT_PREFIX = DateTimeFormatter.ofPattern("yyMMddHHmmssSS");

    private static final long MAX_NUM = 999999999L;
    private static final Integer START_NUM = 1;

    /**
     * @Author dss
     * @Description 获取22位的序列号
     * @Date 2020-12-16 11:38
     * @Param
     * @return
     */
    public static String getStringSequence(){
        LocalDateTime dataTime = LocalDateTime.now(ZONE_ID);
        if(SEQ.intValue()>MAX_NUM){
            SEQ.getAndSet(START_NUM);
        }
        return  dataTime.format(DF_FMT_PREFIX)+ getLocalIpSuffix()+SEQ.getAndIncrement();
    }

    private volatile static String IP_SUFFIX = null;
    private static String getLocalIpSuffix (){
        if(null != IP_SUFFIX){
            return IP_SUFFIX;
        }
        try {
            synchronized (SequenceUtils.class){
                if(null != IP_SUFFIX){
                    return IP_SUFFIX;
                }
                InetAddress addr = InetAddress.getLocalHost();
                String hostAddress = addr.getHostAddress();
                if (null != hostAddress && hostAddress.length() > 4) {
                    String ipSuffix = hostAddress.trim().split("\\.")[3];
                    if (ipSuffix.length() == 2) {
                        IP_SUFFIX = ipSuffix;
                        return IP_SUFFIX;
                    }
                    ipSuffix = "0" + ipSuffix;
                    IP_SUFFIX = ipSuffix.substring(ipSuffix.length() - 2);
                    return IP_SUFFIX;
                }
                IP_SUFFIX = RandomUtils.nextInt(new Random(10), 20) + "";
                return IP_SUFFIX;
            }
        }catch (Exception e){
            System.out.println("获取IP失败:"+e.getMessage());
            IP_SUFFIX =  RandomUtils.nextInt(new Random(10),20)+"";
            return IP_SUFFIX;
        }
    }


    public static void main(String[] args) {
        List<String> orderNos = Collections.synchronizedList(new ArrayList<String>());
        IntStream.range(0,800000).parallel().forEach(i->{
            orderNos.add(getStringSequence());
        });

        List<String> filterOrderNos = orderNos.stream().distinct().collect(Collectors.toList());

        System.out.println("序列号样例："+ orderNos.get(22));
        System.out.println("生成序列号数："+orderNos.size());
        System.out.println("过滤重复后序列号数："+filterOrderNos.size());
        System.out.println("重复序列号数："+(orderNos.size()-filterOrderNos.size()));
    }

}
