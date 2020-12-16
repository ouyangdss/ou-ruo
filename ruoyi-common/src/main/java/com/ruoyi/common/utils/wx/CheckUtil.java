package com.ruoyi.common.utils.wx;

import java.util.Arrays;

public class CheckUtil {
    public static final String token = "##";

    public static boolean check(String signature, String timestamp, String nonce) {
        String arrs[] = {token, timestamp, nonce};
        Arrays.sort(arrs);//字典排序
        //拼接字符串
        StringBuffer sb = new StringBuffer();
        for (String str : arrs) {
            sb.append(str);
        }
        String signaturesha1 = Sha1Util.encode(sb.toString());
        return signaturesha1.equals(signature);
    }
}
