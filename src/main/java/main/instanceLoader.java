package main;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Administrator on 27-11-2015.
 */
public final class instanceLoader {

    public static String getNext() {
        File file = new File("raw.xml");
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[(int) file.length()];
        try {
            fis.read(data);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = null;
        try {
            str = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String sub = str.substring(0, str.indexOf("\n"));

        System.out.println(sub);
        str = str.substring(str.indexOf("\n")+1);


        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        out.print(str);
        out.flush();
        out.close();
        return null;

    }


}
