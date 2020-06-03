import java.io.File;
import java.lang.reflect.Field;

/**
 * 描述：
 * Author：liuxing
 * Date：2020/6/3
 */
public class FileUtils {

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\liuxing\\Desktop\\pic";
        String path2 = "C:\\Users\\liuxing\\Desktop\\pic2";
        File file = new File(path);
        File[] files = file.listFiles();
        for (File f : files) {
            String newFileName = getFileName(path2, f.getName(), 1);
            newFileName += ".jpg";
            f.renameTo(new File(path2 + "/" + newFileName));
        }
    }


    private static String getFileName(String dir, String fileName, int i) {
        String timestamp_ = fileName.substring(fileName.indexOf("timestamp_") + "timestamp_".length());
        String substring = timestamp_.substring(0, timestamp_.indexOf("_"));

        if (new File(dir + "/" + substring + "_" + i + ".jpg ").exists()) {
            return getFileName(dir, fileName, i + 1);
        }
        return substring + "_" + i;
    }
}
