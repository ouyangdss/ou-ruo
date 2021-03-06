package com.ruoyi.web.controller.wx.inputHandler;

import com.ruoyi.common.wx.entity.ReceiveXmlEntity;
import com.ruoyi.web.controller.wx.outputHandler.OutXmlProcess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 图片输入处理类
 *
 * @author ldk
 */
@Component
public class ImageInputHandler implements WXInputHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private OutXmlProcess outXmlProcess;

    @Override
    public String handleWithReceive(ReceiveXmlEntity receiveXmlEntity) {
        String openId = receiveXmlEntity.getFromUserName();
        logger.info("----------------收到用户：openId=" + openId + " 的图片消息----------------");
        StringBuffer content = new StringBuffer();
        content.append("/::D 已收到图片消息，谢谢。");
        return outXmlProcess.getTextResult(receiveXmlEntity, content.toString());
    }

}
