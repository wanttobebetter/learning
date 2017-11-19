package com.nowcoder.service;

import java.io.IOException;
import java.util.UUID;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.ToutiaoUtil;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

@Service
public class QiniuService {
	private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);
	String ACCESS_KEY = "fNT_ACkTvrksHLw921br3O4VIkJhvXGmDxlD6DMK";
	String SECRET_KEY = "ni2anE0gpXKT1c5vU85EKQRvxqi3zGw9MFSIPJ4F";

	// 要上传的空间
	String bucketname = "nowcoder";

	// 密钥配置
	Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);
	//第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
    Zone z = Zone.autoZone();
    Configuration c = new Configuration(z);
	// 创建上传对象
	UploadManager uploadManager = new UploadManager(c);

	 //private static String QINIU_DOMAIN_FREFIX = "http://ozma2ey9f.bkf.clouddn.com/";

	// 简单上传，使用默认策略，只需要设置上传的空间名就可以了
	public String getUpToken() {
		return auth.uploadToken(bucketname);
	}

	public String saveImage(MultipartFile file) throws IOException {
		try {
			int dotPos = file.getOriginalFilename().lastIndexOf(".");
			if (dotPos < 0) {
				return null;
			}
			String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
			if (!ToutiaoUtil.isFileAllowed(fileExt)) {
				return null;
			}

			String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;
			// 调用put方法上传
			Response res = uploadManager.put(file.getBytes(), fileName, getUpToken());
			// 打印返回的信息
			System.out.println(res.toString());
			 if (res.isOK() && res.isJson()) {
				 String key=JSONObject.parseObject(res.bodyString()).get("key").toString();
			 return ToutiaoUtil.QINIU_DOMAIN_FREFIX +key; 
			
			 } else {
			 logger.error("七牛异常:" + res.bodyString());
			 return null;
			 }
//			System.out.println(res.bodyString());
//			return null;
		} catch (QiniuException e) {
			// 请求失败时打印的异常的信息
			logger.error("七牛异常:" + e.getMessage());
			return null;
		}
	}
}
