package test;

import com.megvii.utils.OKHttpUtils;

/**
 * 描述：
 * Author：liuxing
 * Date：2020-09-21
 */
public class UploadCoreObjectUtilTest {

    public static void main(String[] args) {
//        UploadCoreObjectUtil.upload("http://10.122.101.59:8080/v5/objectStorage/testBucLX","C:\\Users\\liuxing\\Desktop\\1.jpg");
        OKHttpUtils.postFile("http://10.122.101.59:8080/v5/objectStorage/testBucLX2", "C:\\Users\\liuxing\\Desktop\\照片\\iYhiIhi3h.jpg", null);
    }
}
