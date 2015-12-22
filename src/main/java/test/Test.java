package test;




import main.DataManager2;
import main.FeatureSelector;
import main.Word;

import java.io.*;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by René Boschma on 2-12-2015.
 */
public class Test {

    public static void main(String[] args){
//        System.out.println("trucount: " + DataManager.getWordcountTrue());
//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter("the-file-name.txt", "UTF-8");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        writer.println("The first line");
//        writer.println("The second line");
//        writer.close();
//        System.out.println(getDefaultCharSet());
//        System.out.println(Arrays.toString(Word.sanitize("Dit is z'n een Test")));
//        System.out.println(Arrays.toString(Word.sanitize("Dït is René Zijn T:e%^&*(st Ë")));
//        //new File("test.txt");
        FeatureSelector.getChiWordList(DataManager2.INSTANCE.getWordList());
    }


    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }

}


