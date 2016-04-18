package com.airsupply.util;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientPost {

	static HttpClientContext context = null;

	public static String getResponse(String url) throws Exception {
		 url=URLEncoder.encode(url,"UTF-8");

		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

			public String handleResponse(final HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
				if (status >= 200 && status < 300) {
					System.out.println(status);
					HttpEntity entity = response.getEntity();
					return entity != null ? EntityUtils.toString(entity) : null;
				} else {
					throw new ClientProtocolException(
							"Unexpected response status: " + status);
				}
			}

		};
		HttpClientContext context = HttpClientContext.create();
	

		HttpGet httpGet = new HttpGet(url);
		
		CloseableHttpClient httpclient = HttpClients.custom().build();

		String sendMsgResponse = httpclient.execute(httpGet, responseHandler,
				context);
		System.out.println(sendMsgResponse);

		
		httpclient.close();
		return sendMsgResponse;

	}

	

}