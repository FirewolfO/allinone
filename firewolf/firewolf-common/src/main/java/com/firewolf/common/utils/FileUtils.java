package com.firewolf.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 描述：文件工具
 * Author：liuxing
 * Date：2020/5/11
 */
public class FileUtils {


    /**
     * 将网络文件存到本地
     *
     * @param fileUrl
     * @param filePath
     * @return
     */
    public static void urlFile2Local(String fileUrl, String filePath) {
        if (fileUrl == null || fileUrl.length() == 0) {
            return;
        }
        OutputStream out = null;

        try {
            out = new FileOutputStream(filePath);
            // 创建URL
            URL url = new URL(fileUrl);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                out.write(by, 0, len);
                out.flush();
            }
            // 关闭流
            if (is != null) {
                is.close();
            }
            out.close();
        } catch (Exception e) {
            Logger.consoleErr("save file to local error:{}", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 将网络文件转换成临时文件，返回文件本身
     *
     * @param url           网络文件地址
     * @param tmpFilePrefix 文件名前缀
     * @param tmpFileSuffix 文件名后缀
     * @return
     * @throws Exception
     */
    public static File url2TmpFile(String url, String tmpFilePrefix, String tmpFileSuffix) throws Exception {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                File file = File.createTempFile(tmpFilePrefix, tmpFileSuffix);
                file.deleteOnExit();
                InputStream inputStream = connection.getInputStream();
                IOUtils.copy(inputStream, new FileOutputStream(file));
                return file;
            }
        } catch (Exception e) {
            Logger.consoleErr("get inputstream error ! error : {}", e);
            throw e;
        }
        return null;
    }

    /**
     * 把网络url文件读取到byte数组中
     *
     * @param fileUrl 文件URL
     * @return
     */
    public static byte[] urlFile2Bytes(String fileUrl) {
        if (fileUrl == null || fileUrl.length() == 0) {
            return null;
        }
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            // 创建URL
            URL url = new URL(fileUrl);
            byte[] by = new byte[1024];
            // 创建链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            InputStream is = conn.getInputStream();
            // 将内容读取内存中
            int len = -1;
            while ((len = is.read(by)) != -1) {
                data.write(by, 0, len);
            }
            // 关闭流
            if (is != null) {
                is.close();
            }
            return data.toByteArray();
        } catch (Exception e) {
            Logger.consoleErr("read file content error : {}", e);
            return null;
        } finally {
            if (data != null) {
                try {
                    data.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    /**
     * 把在线文件转成base64编码字符串
     *
     * @param fileUrl 文件URL
     * @return 编码后的字符串
     */
    public static String encodeURLFile2Base64(String fileUrl) {

        byte[] bytes = urlFile2Bytes(fileUrl);
        if (bytes == null) {
            return null;
        }
        // 对字节数组Base64编码
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }


    /**
     * @param base64Str 文件的base64编码字符串
     * @param path      要生成的文件路径
     * @return
     */
    public static boolean decodeBase64ToFile(String base64Str, String path) {
        if (base64Str == null || base64Str.length() == 0) {
            return false;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            //解密
            byte[] b = decoder.decode(base64Str);
            //处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            if (out != null) {
                out.close();
            }
            return true;
        } catch (IOException e) {
            Logger.consoleErr("trans base64 string to file error:{}", e);
            return false;
        }
    }


    /**
     * 把本地文件转换成byte数组
     *
     * @param pathStr 文件路径
     * @return
     */
    public static byte[] localFile2Bytes(String pathStr) {
        File file = new File(pathStr);
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();
            return data;
        } catch (Exception e) {
            Logger.consoleErr("trans file to byte array error:{}", e);
        }
        return null;
    }


    private static final int BUFFER_SIZE = 2048;


    /**
     * 讲文件压缩成zip包，
     *
     * @param srcDir           原文件假
     * @param destFile         压缩后的文件名
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    public static void toZip(String srcDir, String destFile, boolean KeepDirStructure) {
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            OutputStream out = new FileOutputStream(destFile);
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) { // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }


    /**
     * 把字符串写入到文件中
     *
     * @param content
     * @param filePath
     */
    public static void writeContent2File(String content, String filePath) {
        FileWriter fw = null;
        BufferedWriter wb = null;
        try {
            fw = new FileWriter(filePath);
            wb = new BufferedWriter(fw);
            wb.write(content);
            wb.flush();

        } catch (Exception e) {
            Logger.consoleErr("log: write log error ! {} ", e);
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                }
                if (wb != null) {
                    wb.close();
                }
            } catch (Exception e) {
                Logger.consoleErr("close stream error!, {} ", e);
            }
        }

    }

}
