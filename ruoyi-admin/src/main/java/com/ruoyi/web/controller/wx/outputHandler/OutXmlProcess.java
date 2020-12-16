package com.ruoyi.web.controller.wx.outputHandler;


import com.ruoyi.common.wx.entity.ArticleItem;
import com.ruoyi.common.wx.entity.ReceiveXmlEntity;
import com.ruoyi.common.wx.entity.SendXmlEntity;
import com.ruoyi.web.controller.wx.core.WXBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于创建输出xml文本的处理器。
 *
 * @author ldk
 */
@Component
public class OutXmlProcess {
    @Autowired
    private WXBeanFactory wxBeanFactory;

    /**
     * 通过接收XML实体类对象和设定好的发送文本，确定发送Text类型的XML文本。
     *
     * @param receiveXmlEntity 接受XML实体对象。
     * @param respContent      设定好的发送文本。
     * @return 要发送的Xml文本
     */
    public String getTextResult(ReceiveXmlEntity receiveXmlEntity, String respContent) {
        //对发送Xml实体对象进行确定
        SendXmlEntity sendXmlEntity = getSendXmlEntity(receiveXmlEntity);
        sendXmlEntity.setMsgType("text");
        sendXmlEntity.setContent(respContent);

        return wxBeanFactory.getTextOutputHandler().getXmlResult(sendXmlEntity);
    }

    /**
     * 通过接收XML实体类对象和设定好的发送文本，确定发送Text类型的XML文本。
     *
     * @param receiveXmlEntity 接受XML实体对象。
     * @param respContent      设定好的发送文本。
     * @return 要发送的Xml文本
     */
    public String getImgResult(ReceiveXmlEntity receiveXmlEntity, String respContent) {
        //对发送Xml实体对象进行确定
        SendXmlEntity sendXmlEntity = getSendXmlEntity(receiveXmlEntity);
        sendXmlEntity.setMsgType("image");
        sendXmlEntity.setContent(respContent);

        return wxBeanFactory.getImageOutputHandler().getXmlResult(sendXmlEntity);
    }

    /**
     * 通过接收XML实体类对象和设定好的发送文本，确定发送图文类型的XML文本。
     *
     * @param receiveXmlEntity 接受XML实体对象。
     * @param articleItems     图文消息信息（ArticleItem）的List。
     * @return 要发送的Xml文本
     */
    public String getArticlesResult(ReceiveXmlEntity receiveXmlEntity, List<ArticleItem> articleItems) {
        //对发送Xml实体对象进行确定
        SendXmlEntity sendXmlEntity = getSendXmlEntity(receiveXmlEntity);
        sendXmlEntity.setMsgType("news");
        sendXmlEntity.setArticleCount(Integer.toString(articleItems.size()));
        sendXmlEntity.setArticles(articleItems);

        return wxBeanFactory.getArticlesOutputHandler().getXmlResult(sendXmlEntity);
    }

    /**
     * 通过接收XML实体类对象和设定好的发送文本，确定发送链接类型的XML文本。
     *
     * @param receiveXmlEntity 接受XML实体对象。
     * @param title            图文消息标题
     * @param description      图文消息描述
     * @param url              点击图文消息跳转链接
     * @return 要发送的Xml文本
     */
    public String getLinkResult(ReceiveXmlEntity receiveXmlEntity, String title, String description, String url) {
        //对发送Xml实体对象进行确定
        SendXmlEntity sendXmlEntity = getSendXmlEntity(receiveXmlEntity);
        sendXmlEntity.setMsgType("link");
        sendXmlEntity.setTitle(title);
        sendXmlEntity.setDescription(description);
        sendXmlEntity.setUrl(url);
        sendXmlEntity.setMsgId(receiveXmlEntity.getMsgId());

        return wxBeanFactory.getLinkOutputHandler().getXmlResult(sendXmlEntity);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * 根据输入xml对象，创建输出xml对象，并填充基础属性。
     *
     * @param receiveXmlEntity
     * @return
     */
    private SendXmlEntity getSendXmlEntity(ReceiveXmlEntity receiveXmlEntity) {
        SendXmlEntity sendXmlEntity = new SendXmlEntity();
        sendXmlEntity.setFromUserName(receiveXmlEntity.getToUserName());
        sendXmlEntity.setToUserName(receiveXmlEntity.getFromUserName());
        sendXmlEntity.setCreateTime(receiveXmlEntity.getCreateTime());
        return sendXmlEntity;
    }
}
