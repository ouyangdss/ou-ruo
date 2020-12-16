package com.ruoyi.quartz.task;

import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.wx.WXUtil;
import com.ruoyi.common.wx.entity.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("obtainAccessTokenTask")
public class ObtainAccessTokenTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void obtainAccessToken(String appId, String secret) {
        // 调用接口获取access_token
        AccessToken accessToken = WXUtil.getAccessToken(appId, secret);
        if (null != accessToken) {
            String jsapi_ticket = WXUtil.getJsApiTicket(accessToken.getToken());

            CacheUtils.put("accessToken", accessToken);
            CacheUtils.put("jsapi_ticket", jsapi_ticket);
            logger.info("获取accessToken：" + accessToken.getToken() + " 成功！\n"
                    + "获取jsapi_ticket：" + jsapi_ticket + " 成功！");
        } else {
            logger.info("获取accessToken： 失败！\n");
        }

    }
}
