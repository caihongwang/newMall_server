package com.br.newMall.center.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomMessageUtil {

    private static Logger log = LoggerFactory.getLogger(CustomMessageUtil.class);

    /**
     * 组装发送文本链接消息
     * 小程序方式，公众号也支持
     * @return
     */
    public static String makeTextCustomMessage(String touser, String content) {
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"text\",\"text\":{\"content\":\"%s\"}}";
        return String.format(jsonMsg, touser, content);
    }

    /**
     * 组装发送图片链接消息
     * 小程序方式，公众号也支持
     * @return
     */
    public static String makeImageCustomMessage(String touser, String title, String description, String imgUrl) {
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"link\",\"link\":{\"title\":\"%s\",\"description\":\"%s\",\"url\":\"%s\",\"thumb_url\":\"%s\"}}";
        return String.format(jsonMsg, touser, title, description, imgUrl, imgUrl);
    }

    /**
     * 组装发送小程序卡片消息
     * 小程序方式，公众号也支持
     * @return
     */
    public static String makeMiniProgramPageCustomMessage(String touser, String title, String appid, String pagePath, String thumbMediaId) {
        String jsonMsg = "{\"touser\":\"%s\",\"msgtype\":\"miniprogrampage\",\"miniprogrampage\": {\"title\":\"%s\",\"appid\":\"%s\",\"pagepath\":\"%s\",\"thumb_media_id\":\"%s\"}}";
        return String.format(jsonMsg, touser, title, appid, pagePath, thumbMediaId);
    }

    /**
     * 群发-组装发送图文消息
     *
     * @return
     */
    public static String makeTextCustomMessageForAll(String touser, String mediaId) {
        String jsonMsg = "{\"touser\":%s,\"msgtype\":\"mpnews\",\"send_ignore_reprint\":\"0\",\"mpnews\":{\"media_id\":\"%s\"}}";
        return String.format(jsonMsg, touser, mediaId);
    }
}
