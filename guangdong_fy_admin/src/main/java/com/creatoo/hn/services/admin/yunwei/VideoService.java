package com.creatoo.hn.services.admin.yunwei;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.creatoo.hn.services.BaseService;
import com.creatoo.hn.util.AliyunOssUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数字资源服务类
 * @author wangxl
 * @version 20161108
 */
@Service
public class VideoService extends BaseService{
	/**
	 * 日志控制器
	 */
	Logger log = Logger.getLogger(this.getClass());

	
	/**
	 * 删除数字资源
	 * @param keys
	 * @throws Exception
	 */
	public void delete(String keys)throws Exception{
		OSSClient ossClient = null;
		try {
			// 创建OSSClient实例
			String endpoint = AliyunOssUtil.getEndpoint();// endpoint以杭州为例，其它region请按实际情况填写
			String accessKeyId = AliyunOssUtil.getAccessKeyId();// accessKey请登录https://ak-console.aliyun.com/#/查看
			String accessKeySecret = AliyunOssUtil.getAccessKeySecret();
			String bucketName = AliyunOssUtil.getBucketName();
			ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			
			List<String> _keys = new ArrayList<String>();
			if(keys != null){
				String[] keyArr = keys.split(",");
				for(String key : keyArr){
					if(!key.isEmpty()){
						if(key.endsWith("/")){
							_keys.addAll(AliyunOssUtil.listKeys(key));
						}else{
							_keys.add(key);
						}
					}
				}
			}
			
			ossClient.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(_keys));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(ossClient != null){
				ossClient.shutdown();
			}
		}
	}
	
	/**
	 * 分页查询数字资源
	 * @param param 参数条件
	 * @return 分页内容
	 * @throws Exception
	 */
	public Map<String, Object> srchPagging(Map<String, Object> param)throws Exception{
		List<Map<String, Object>> resultMap = new ArrayList<Map<String, Object>>();
		if(param.containsKey("srchFile") && "1".equals(param.get("srchFile"))){//查询指定目录下的资源
			String dir = (String)param.get("dir");
			if(dir == null || dir.isEmpty()){
				dir = "root";
			}
			resultMap = AliyunOssUtil.listAllFile4Dir(dir);
		}else if(param.containsKey("srchDir") && "1".equals(param.get("srchDir"))){//查询所有目录
			resultMap = AliyunOssUtil.listAllDir("root");
		}else if(param.containsKey("dir")){//查询指定目录下的资源-后台视频管理列表查询页用到
			String dir = (String)param.get("dir");//默认是root
			String prefix = (String)param.get("keyname");
			resultMap = AliyunOssUtil.listAllObject(dir, prefix);
		}else{//查询所有资源-不含目录
			resultMap = AliyunOssUtil.listAllObject4Data();
		}
       
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        rtnMap.put("total", resultMap.size());
        rtnMap.put("rows", resultMap);
		return rtnMap;
	};

	/**
	 * 查询OSS资源
	 * @param cultid 文化馆ID
	 * @param dir 指定目录
	 * @param keyname 关键字
	 * @param srchDir 查询目录
	 * @param srchFile 查询目录下的文件
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> srchPagging(String cultid, String dir, String keyname, String srchFile, String srchDir)throws Exception{
		//如果文化馆对应的目录不存在/创建
		if (StringUtils.isNotEmpty(cultid)) {
			AliyunOssUtil.validAndCreateDir(cultid);
		}

		List<Map<String, Object>> resultMap = new ArrayList<>();
		if("1".equals(srchDir)){//查询所有目录
			resultMap = AliyunOssUtil.listAllDir(cultid+"/");
		}else if("1".equals(srchFile)){//查询指定目录下的资源
			resultMap = AliyunOssUtil.listAllFile4Dir(dir);
		}else{//查询目录和资源-只有一级
			if(StringUtils.isEmpty(dir) || "root".equals(dir)){
				dir = cultid;
			}

			resultMap = AliyunOssUtil.listAllObject(dir, keyname);

			Map<String, Object> curtDir = null;
			for(Map<String, Object> map : resultMap){
				String key = (String) map.get("key");
				if(key.equals(cultid)){
					curtDir = map;
				}else{
					//不显示文化馆根目录名称
					map.put("keyValue", key.substring(cultid.length()));
				}
			}
			if(curtDir != null){
				resultMap.remove(curtDir);
			}
		}

		//返回
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		rtnMap.put("total", resultMap.size());
		rtnMap.put("rows", resultMap);
		return rtnMap;
	}
}
