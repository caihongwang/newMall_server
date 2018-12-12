package com.br.newMall.center.utils;

/**
 * 上传的临时多媒体文件有格式
 * 分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
 */
public enum MediaTypeUtil {

    IMAGE, VOICE, VIDEO, THUMB, NEWS;

    public static MediaTypeUtil getMediaTypeUtil(String mediaType){
        if("IMAGE".equals(mediaType)){
            return IMAGE;
        } else if("VOICE".equals(mediaType)){
            return VOICE;
        } else if("VIDEO".equals(mediaType)){
            return VIDEO;
        } else if("THUMB".equals(mediaType)){
            return THUMB;
        } else if("NEWS".equals(mediaType)){
            return NEWS;
        } else {
            return NEWS;
        }
    }

    // 转化成小写形式
    public String get() {
        return this.name().toLowerCase();
    }
}
