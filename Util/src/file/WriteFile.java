package file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class WriteFile{
    
    public static void main(String[] args) {
        String classPath = WriteFile.class.getResource("").getPath(); //当前文件路径
        System.out.println(classPath);
        File file = new File("test.txt");
        if (!file.exists()) {
            try {
                file.createNewFile(); //如果文件不存在则创建文件
            } catch (IOException e) {
                e.printStackTrace();
            }  
        }
        writeInFile(file, "test");   //写入文件
    }
    
    private static void writeInFile(File file, String content) {
        Writer writer = null;
        StringBuilder outputString = new StringBuilder();
        try {
            outputString.append(content + "\r\n");
            writer = new FileWriter(file, true); // true表示追加
            writer.write(outputString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }
}