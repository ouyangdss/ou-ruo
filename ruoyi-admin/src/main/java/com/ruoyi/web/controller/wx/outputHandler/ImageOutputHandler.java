package com.ruoyi.web.controller.wx.outputHandler;

import com.ruoyi.common.wx.entity.SendXmlEntity;
import org.springframework.stereotype.Component;

/**
 * 图片输出处理类
 *
 * @author ldk
 */
@Component
public class ImageOutputHandler implements WXOutputHandler {

    @Override
    public String getXmlResult(SendXmlEntity sendXmlEntity) {
        StringBuffer result = new StringBuffer();
        result.append("<xml><ToUserName><![CDATA[");
        result.append(sendXmlEntity.getToUserName());
        result.append("]]></ToUserName><FromUserName><![CDATA[");
        result.append(sendXmlEntity.getFromUserName());
        result.append("]]></FromUserName><CreateTime>");
        result.append(sendXmlEntity.getCreateTime());
        result.append("</CreateTime><MsgType><![CDATA[");
        result.append(sendXmlEntity.getMsgType());
        result.append("]]></MsgType><Image><MediaId><![CDATA[");
        result.append(sendXmlEntity.getContent());
        result.append("]]></MediaId></Image></xml>");
        return result.toString();
    }
}
