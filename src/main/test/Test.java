package main.test;


import main.DataManager;
import main.GUI;
import main.Word;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by Ren� Boschma on 2-12-2015.
 */
public class Test {

    public static void main(String[] args){
        System.out.println("trucount: " + DataManager.getWordcountTrue());

        System.out.println(getDefaultCharSet());
        System.out.println(Arrays.toString(Word.sanitize("Dit is z'n een Test")));
        System.out.println(Arrays.toString(Word.sanitize("D�t is Ren� Zijn T:e%^&*(st �")));
    }


    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }

}


