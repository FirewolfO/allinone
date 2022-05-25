package test;

import com.megvii.sign.SignUtlis;
import com.megvii.utils.OKHttpUtils;

/**
 * 描述： 模拟一次请求的过程
 * Author：liuxing
 * Date：2020-09-14
 */
public class SignUtilTest {

    public static void main(String[] args) {
        String ctimestamp = "1599648837833"; // 时间戳，用户自己生成
        String cnonce = "123456"; // 6位随机码，用户自己生成
        String requestParam = ""; //请求参数，form表单方式，盘古大部分接口都是body传参，故这里大部分都是""，如果有参数，查看文档说明
        String url = "/v1/api/device/list"; // 要请求的url,看要请求的接口说明
        String method = "POST"; // 请求方式,看要请求的接口说明
        String cappKey = "appkey1"; // appkey,固定，
        String secret = "sdfajk3242324fa!djq7"; // 秘钥，固定

        DeviceReq deviceReq = DeviceReq.builder().name("core").build(); // 构建请求体，看要请求的接口说明

        // 计算签名
        String csign = SignUtlis.sign(ctimestamp, cnonce, deviceReq, requestParam, url, method, cappKey, secret);

        // 发起请求
        String realUrl = "http://10.122.101.181:18082" + url;
        postJson(ctimestamp, cnonce, cappKey, csign, deviceReq, realUrl);

    }


    private static void postJson(String ctimestamp, String cnonce, String cappKey, String csign, Object reqeustData, String url) {
        OKHttpUtils.postJson(url, reqeustData, requestBuilder ->
                requestBuilder.addHeader("cnonce", cnonce)
                        .addHeader("ctimestamp", ctimestamp)
                        .addHeader("cappKey", cappKey)
                        .addHeader("csign", csign));
    }
}
