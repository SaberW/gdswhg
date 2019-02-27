package com.creatoo.hn.util.ueditor;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.baidu.ueditor.upload.StorageManager;
import com.creatoo.hn.controller.BaseController;
import com.creatoo.hn.util.ENVUtils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.env.Environment;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader{

	public static final State save(HttpServletRequest request,
								   Map<String, Object> conf) {
		FileItemStream fileStream = null;
		boolean isAjaxUpload = request.getHeader( "X_Requested_With" ) != null;
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}
		ServletFileUpload upload = new ServletFileUpload(
				new DiskFileItemFactory());

		if ( isAjaxUpload ) {
			upload.setHeaderEncoding( "UTF-8" );
		}

		try {
			FileItemIterator iterator = upload.getItemIterator(request);
			while (iterator.hasNext()) {
				fileStream = iterator.next();

				if (!fileStream.isFormField())
					break;
				fileStream = null;
			}

			if (fileStream == null) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}
//			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//			MultipartFile multipartFile = multipartRequest.getFile(conf.get("fieldName").toString());
//			while (iterator.hasNext()) {
//				fileStream = iterator.next();
//
//				if (!fileStream.isFormField())
//					break;
//				fileStream = null;
//			}
//
//			if (multipartFile == null) {
//				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
//			}
			String savePath1 = (String) conf.get("savePath");
			//String savePath = PropertiesReadUtils.getInstance().getPropValueByKey("basePath")+savePath1;
			String savePath = ENVUtils.env.getProperty("upload.local.addr")+ File.separator+savePath1 ;//env.getProperty("upload.local.addr") ;
//			String originFileName = multipartFile.getOriginalFilename();
			String originFileName = fileStream.getName();
			String suffix = FileType.getSuffixByFilename(originFileName);

			originFileName = originFileName.substring(0,
					originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String physicalPath = savePath; //(String) conf.get("rootPath") +

			InputStream is = fileStream.openStream();
//			State storageState = StorageManager.saveFileByInputStream(multipartFile.getInputStream(),
//					physicalPath, maxSize);
			State storageState = StorageManager.saveFileByInputStream(is,
					physicalPath, maxSize);
			is.close();

			if (storageState.isSuccess()) {
//				String basePath = PropertiesReadUtils.getInstance().getPropValueByKey("basePath") ;
				String basePath = ENVUtils.env.getProperty("upload.local.addr") ;
				Integer port = request.getServerPort() ;
				String domainUrl = ENVUtils.env.getProperty("upload.local.server.addr") ;
				/*if(port == 80) {
					domainUrl = request.getScheme() + "://"+ request.getServerName()  + "/whgstatic/" ;
				} else {
					domainUrl = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort() + "/whgstatic/";
				}*/
				String staticServierUrl = domainUrl+"/";//PropertiesReadUtils.getInstance().getPropValueByKey("staticServerUrl") ;
				storageState.putInfo("url", savePath.replace(basePath,staticServierUrl));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}

			return storageState;
		} catch (FileUploadException e) {
			return new BaseState(false, AppInfo.PARSE_REQUEST_ERROR);
		} catch (IOException e) {
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);

		return list.contains(type);
	}
}
