package com.megvii.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-21
 */
public class OKHttpUtils {

    private static Slf4jLog log = new Slf4jLog();


    /**
     * post提交json
     *
     * @param url
     * @param reqeustData
     * @param requestAdjustHandler
     * @return
     */
    public static String postJson(String url, Object reqeustData, Consumer<Request.Builder> requestAdjustHandler) {
        String bodyJson = JSONObject.toJSONString(reqeustData);
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        RequestBody requestBody = RequestBody.create(mediaType, bodyJson);
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(requestBody);

        if (requestAdjustHandler != null) {
            requestAdjustHandler.accept(builder);
        }
        Request request = builder.build();
        return doPost(request);
    }


    /**
     * 文件上传
     *
     * @param url
     * @param localFilePath
     * @param requestAdjustHandler
     * @return
     */
    public static String postFile(String url, String localFilePath, Consumer<Request.Builder> requestAdjustHandler) {
        File file = new File(localFilePath);
        MediaType mediaType = MediaType.parse("application/octet-stream");
        RequestBody req = MultipartBody.create(file, mediaType);
        Request.Builder builder = new Request.Builder();
        builder
                .url(url)
                .post(req);
        if (requestAdjustHandler != null) {
            requestAdjustHandler.accept(builder);
        }
        Request request = builder.build();
        return doPost(request);
    }


    /**
     * post提交
     *
     * @param request
     * @return
     */
    private static String doPost(Request request) {
        OkHttpClient httpClient = getHttpClient();
        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
            String result = response.body().string();
            System.out.println("请求结果：" + result);
            System.out.println(response);
            return result;
        } catch (Exception e) {
            log.error("post error!", e);
        }
        return null;
    }

    private static OkHttpClient getHttpClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .readTimeout(30000, TimeUnit.SECONDS)
                .writeTimeout(30000, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }
}
