package file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public class ReadFile {
    public static void main(String[] args) {

        File file = new File("./test.txt");
        if (!file.exists()) {
            System.out.println("文件不存在!");
            return ;
        }
        try {
            readtFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /** 读取数据，存入集合中 */
    public static void readtFile(File file) throws IOException, ParseException {
        InputStreamReader read = null;// 考虑到编码格式
        try {
            read = new InputStreamReader(new FileInputStream(file), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(read);
        String lineTxt = null;
        while ((lineTxt = bufferedReader.readLine()) != null) {
            System.out.println(lineTxt);
        }
        read.close();
    }
}