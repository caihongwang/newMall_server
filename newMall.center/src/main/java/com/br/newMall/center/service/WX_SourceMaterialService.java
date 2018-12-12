package com.br.newMall.center.service;

import com.br.newMall.api.dto.ResultMapDTO;
import java.util.Map;

/**
 * 素材管理
 */
public interface WX_SourceMaterialService {

    /**
     * 新增临时素材
     * 图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式
     * 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     * 视频（video）：10MB，支持MP4格式
     * 缩略图（thumb）：64KB，支持JPG格式
     * @param paramMap
     * @return
     */
    public ResultMapDTO uploadMedia(Map<String, Object> paramMap);

    /**
     * 获取临时素材
     * 注意: 视频文件不支持https下载，调用该接口需http协议。
     * @param paramMap
     * @return
     */
    public ResultMapDTO getMedia(Map<String, Object> paramMap);

    /**
     * 新增其他类型永久素材
     * @param paramMap
     * @return
     */
    public ResultMapDTO addMaterial(Map<String, Object> paramMap);

    /**
     * 新增永久素材
     * 图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式
     * 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     * 视频（video）：10MB，支持MP4格式
     * 缩略图（thumb）：64KB，支持JPG格式
     * @param paramMap
     * @return
     */
    public ResultMapDTO addNews(Map<String, Object> paramMap);

    /**
     * 获取永久素材
     * 根据mediaId下载素材，并保存在filePath路径之下
     * @param paramMap
     * @return
     */
    public ResultMapDTO getMaterial(Map<String, Object> paramMap);

    /**
     * 删除永久素材
     * @param paramMap
     * @return
     */
    public ResultMapDTO delMaterial(Map<String, Object> paramMap);


    /**
     * 修改永久图文素材
     * @param paramMap
     * @return
     */
    public ResultMapDTO updateNews(Map<String, Object> paramMap);

    /**
     * 获取素材总数
     * @param paramMap
     * @return
     */
    public ResultMapDTO getMaterialCount(Map<String, Object> paramMap);

    /**
     * 获取素材列表
     * @param paramMap
     * @return
     */
    public ResultMapDTO batchGetMaterial(Map<String, Object> paramMap);

}
