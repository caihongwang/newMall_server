package com.br.newMall.center.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.br.newMall.api.code.NewMallCode;
import com.br.newMall.api.dto.ResultMapDTO;
import com.br.newMall.center.service.WX_SourceMaterialService;
import com.br.newMall.center.utils.MapUtil;
import com.br.newMall.center.utils.MediaArticlesUtil;
import com.br.newMall.center.utils.MediaTypeUtil;
import com.br.newMall.center.utils.WX_PublicNumberUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 素材管理service
 */
@Service
public class WX_SourceMaterialServiceImpl implements WX_SourceMaterialService {

    private static final Logger logger = LoggerFactory.getLogger(WX_SourceMaterialServiceImpl.class);

    /**
     * 新增临时素材
     * 图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式
     * 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     * 视频（video）：10MB，支持MP4格式
     * 缩略图（thumb）：64KB，支持JPG格式
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO uploadMedia(Map<String, Object> paramMap) {
        logger.info("在service中新增临时素材-uploadMedia,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String filePath = paramMap.get("filePath")!=null?paramMap.get("filePath").toString():"";
        Object mediaTypeObj = paramMap.get("mediaType")!=null?paramMap.get("mediaType"):null;
        if(!"".equals(filePath) && mediaTypeObj != null){
            MediaTypeUtil mediaType = (MediaTypeUtil)mediaTypeObj;
            File file = new File(filePath);
            resultMap = WX_PublicNumberUtil.uploadMedia(mediaType, file);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中新增临时素材-uploadMedia,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取临时素材
     * 注意: 视频文件不支持https下载，调用该接口需http协议。
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getMedia(Map<String, Object> paramMap) {
        logger.info("在service中获取临时素材-getMedia,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String mediaId = paramMap.get("mediaId")!=null?paramMap.get("mediaId").toString():"";
        if(!"".equals(mediaId)){
            resultMap = WX_PublicNumberUtil.getMedia(mediaId);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中获取临时素材-getMedia,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 新增其他类型永久素材
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO addMaterial(Map<String, Object> paramMap) {
        logger.info("在service中新增其他类型永久素材-addMaterial,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String filePath = paramMap.get("filePath")!=null?paramMap.get("filePath").toString():"";
        Object mediaTypeObj = paramMap.get("mediaType")!=null?paramMap.get("mediaType"):null;
        if(!"".equals(filePath) && mediaTypeObj != null){
            MediaTypeUtil mediaType = (MediaTypeUtil)mediaTypeObj;
            File file = new File(filePath);
            resultMap = WX_PublicNumberUtil.addMaterial(file, mediaType);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中新增其他类型永久素材-addMaterial,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 新增永久素材
     * 图片（image）: 2M，支持PNG\JPEG\JPG\GIF格式
     * 语音（voice）：2M，播放长度不超过60s，支持AMR\MP3格式
     * 视频（video）：10MB，支持MP4格式
     * 缩略图（thumb）：64KB，支持JPG格式
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO addNews(Map<String, Object> paramMap) {
        logger.info("在service中新增永久素材-addNews,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        Object mediaArticlesUtilListObj = paramMap.get("mediaArticlesUtilList")!=null?paramMap.get("mediaArticlesUtilList"):null;
        if(mediaArticlesUtilListObj != null){
            List<MediaArticlesUtil> mediaArticlesUtilList = (List<MediaArticlesUtil>)mediaArticlesUtilListObj;
            resultMap = WX_PublicNumberUtil.addNews(mediaArticlesUtilList);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中新增永久素材-addNews,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取永久素材
     * 根据mediaId下载素材，并保存在filePath路径之下
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getMaterial(Map<String, Object> paramMap) {
        logger.info("在service中获取永久素材-getMaterial,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String mediaId = paramMap.get("mediaId")!=null?paramMap.get("mediaId").toString():"";
        String filePath = paramMap.get("filePath")!=null?paramMap.get("filePath").toString():"";
        if(!"".equals(mediaId) && !"".equals(filePath)){
            resultMap = WX_PublicNumberUtil.getMaterial(mediaId, filePath);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中获取永久素材-getMaterial,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 删除永久素材
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO delMaterial(Map<String, Object> paramMap) {
        logger.info("在service中删除永久素材-delMaterial,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String mediaId = paramMap.get("mediaId")!=null?paramMap.get("mediaId").toString():"";
        if(!"".equals(mediaId)){
            resultMap = WX_PublicNumberUtil.delMaterial(mediaId);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中删除永久素材-delMaterial,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }


    /**
     * 修改永久图文素材
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO updateNews(Map<String, Object> paramMap) {
        logger.info("在service中修改永久图文素材-updateNews,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        String mediaId = paramMap.get("mediaId")!=null?paramMap.get("mediaId").toString():"";
        String index = paramMap.get("index")!=null?paramMap.get("index").toString():"0";
        Object mediaArticlesUtilObj = paramMap.get("mediaArticlesUtil")!=null?paramMap.get("mediaArticlesUtil"):null;
        if(!"".equals(mediaId) && !"".equals(mediaId) && mediaArticlesUtilObj != null){
            MediaArticlesUtil mediaArticlesUtil = (MediaArticlesUtil)mediaArticlesUtilObj;
            resultMap = WX_PublicNumberUtil.updateNews(mediaId, Integer.parseInt(index), mediaArticlesUtil);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中修改永久图文素材-updateNews,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取素材总数
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO getMaterialCount(Map<String, Object> paramMap) {
        logger.info("在service中获取素材总数-getMaterialCount,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        resultMap = WX_PublicNumberUtil.getMaterialCount();
        if (resultMap != null && resultMap.size() > 0) {
            resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
            resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
            resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
        } else {
            resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
            resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
        }
        logger.info("在service中获取素材总数-getMaterialCount,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

    /**
     * 获取素材列表
     * @param paramMap
     * @return
     */
    @Override
    public ResultMapDTO batchGetMaterial(Map<String, Object> paramMap) {
        logger.info("在service中获取素材列表-batchGetMaterial,请求-paramMap:" + JSONObject.toJSONString(paramMap));
        ResultMapDTO resultMapDTO = new ResultMapDTO();
        Map<String, Object> resultMap = Maps.newHashMap();
        List<Map<String, Object>> materialList = Lists.newArrayList();
        String mediaType = paramMap.get("mediaType")!=null?paramMap.get("mediaType").toString():"NEWS";
        String offset = paramMap.get("offset")!=null?paramMap.get("offset").toString():"0";
        String count = paramMap.get("count")!=null?paramMap.get("count").toString():"20";
        if(!"".equals(offset) && !"".equals(count) && !"".equals(mediaType)){
            materialList = WX_PublicNumberUtil.batchGetMaterial(MediaTypeUtil.getMediaTypeUtil(mediaType), Integer.parseInt(offset), Integer.parseInt(count));
            resultMap.put("materialList", materialList);
            if (resultMap != null && resultMap.size() > 0) {
                resultMapDTO.setResultListTotal(materialList.size());
                resultMapDTO.setResultMap(MapUtil.getStringMap(resultMap));
                resultMapDTO.setCode(NewMallCode.SUCCESS.getNo());
                resultMapDTO.setMessage(NewMallCode.SUCCESS.getMessage());
            } else {
                resultMapDTO.setCode(NewMallCode.SERVER_INNER_ERROR.getNo());
                resultMapDTO.setMessage(NewMallCode.SERVER_INNER_ERROR.getMessage());
            }
        } else {
            resultMapDTO.setCode(NewMallCode.PARAM_IS_NULL.getNo());
            resultMapDTO.setMessage(NewMallCode.PARAM_IS_NULL.getMessage());
        }
        logger.info("在service中获取素材列表-batchGetMaterial,响应-response:" + JSONObject.toJSONString(resultMapDTO));
        return resultMapDTO;
    }

}
