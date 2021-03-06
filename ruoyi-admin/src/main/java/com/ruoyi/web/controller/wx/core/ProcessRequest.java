package com.ruoyi.web.controller.wx.core;


import com.ruoyi.common.wx.entity.ReceiveXmlEntity;
import com.ruoyi.util.XMLUtil;
import com.ruoyi.util.XMLUtils;
import com.ruoyi.web.controller.wx.outputHandler.OutXmlProcess;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 对各类请求进行处理的类。
 *
 * @author ldk
 */
@Component
public class ProcessRequest {

    @Autowired
    private OutXmlProcess outXmlProcess;
    @Autowired
    private WXBeanFactory wxBeanFactory;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 根据request请求进行具体的处理
     *
     * @param request  Http请求
     * @param response Http回应
     * @return
     * @throws Exception
     */
    public String process(Map<String, String> reqXmlData, HttpServletResponse response) throws Exception {
        //解析请求Xml文档为map类型容器
        //将map转化为接收Xml实体对象
        ReceiveXmlEntity receiveXmlEntity = new Map2EntityProcess().getMsgEntity(reqXmlData);
        //最终返回的xml文本
        String result = StringUtils.EMPTY;

        String msgType = receiveXmlEntity.getMsgType();
        logger.info("收到消息，消息类型为：" + msgType + "!");
        //文本消息
        if (msgType.equals("text")) {
            result = wxBeanFactory.getTextInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //图片消息
        else if (msgType.equals("image")) {
            result = wxBeanFactory.getImageInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //声音消息
        else if (msgType.equals("voice")) {
            result = wxBeanFactory.getVoiceInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //地理位置
        else if (msgType.equals("location")) {
            result = wxBeanFactory.getLocationInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //视频消息
        else if (msgType.equals("video")) {
            result = wxBeanFactory.getVideoInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //短视频消息
        else if (msgType.equals("shortvideo")) {
            result = wxBeanFactory.getShortVideoInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //链接消息
        else if (msgType.equals("link")) {
            result = wxBeanFactory.getLinkInputHandler().handleWithReceive(receiveXmlEntity);
        }
        //事件类型
        else if (msgType.equals("event")) {
            result = wxBeanFactory.getEventInputHandler().handleWithReceive(receiveXmlEntity);
        }
        return result;
    }


}