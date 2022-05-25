package com.firewolf.pattern.facade;

/**
 * Description: 门面
 * Author: liuxing
 * Email: 1483345163@qq.com
 * Time: 2020/7/4 8:29 下午
 */
public class FileEncoderFacade {

    private FileReader fileReader = new FileReader();
    private StringEncoder stringEncoder = new StringEncoder();
    private FileWriter fileWriter = new FileWriter();

    public void encodeFile(String srcFileName,String destFileName){
        String content = fileReader.readFileContent(srcFileName);
        String encode = stringEncoder.encode(content);
        fileWriter.writeFile(encode,destFileName);
    }
}
