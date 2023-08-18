package com.unitechs.biz.common.utils;

import com.unitechs.framework.logger.Logger;
import com.unitechs.framework.logger.LoggerFactory;
import okhttp3.*;
import okhttp3.FormBody.Builder;

import java.util.Map;
import java.util.Map.Entry;

public class HttpRemoteCallClient {
	
	private static Logger logger = LoggerFactory.getLogger(HttpRemoteCallClient.class);
	
	public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

	public static String getRemote(String remoteUrl) throws Exception {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(remoteUrl).build();
		Response response = client.newCall(request).execute();
		if (response.isSuccessful()) {
			return response.body().string();
		} else {
        	logger.error("调用远程的系统异常(getRemote): "+response);
			throw new Exception("调用远程的系统异常(remoteUrl ="+ remoteUrl + ") :" + response);
		}
	}

	public static String postRemote(String remoteUrl, String json) {
		Response response=null;
		try {
			OkHttpClient client = new OkHttpClient();
			RequestBody body = RequestBody.create(JSON, json);
			Request request = new Request.Builder().url(remoteUrl).post(body).build();
			response = client.newCall(request).execute();
			if (response.isSuccessful()) {
				return response.body().string();
			} else {
				logger.error("调用远程的系统异常(postRemote): "+response);
				response.close();
				throw new Exception("调用远程的系统异常(remoteUrl ="+ remoteUrl + ") :" + response);
			}
		} catch (Exception e) {
			logger.error("调用远程的系统异常(getRemote): "+e);
//			throw new Exception("调用远程的系统异常(remoteUrl ="+ remoteUrl + ") :" + e);
		}finally {
			if(response!=null) {
				response.close();
			}
		}
		return "";
	}
	public static String postRemote(String remoteUrl,Map<String,String> dataMap) throws Exception {
		OkHttpClient client = new OkHttpClient();
		Builder builder=new Builder();
		if(dataMap!=null && !dataMap.isEmpty()) {
			for(Entry<String,String>entry:dataMap.entrySet()) {
				builder.add(entry.getKey(), entry.getValue());
			}
		}
		FormBody formBody = builder.build();
	    Request request = new Request.Builder().url(remoteUrl).post(formBody).build();
	    Response response = client.newCall(request).execute();
	    if (response.isSuccessful()) {
	        return response.body().string();
	    } else {
	    	logger.error("调用远程的系统异常(postRemote): "+response);
			throw new Exception("调用远程的系统异常(remoteUrl ="+ remoteUrl + ") :" + response);
	    }
	}

}