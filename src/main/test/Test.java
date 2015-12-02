package main.test;


import main.Word;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.text.Normalizer;

/**
 * Created by Ren� Boschma on 2-12-2015.
 */
public class Test {

    public static void main(String[] args){
        System.out.println(getDefaultCharSet());
        System.out.println(Normalizer.normalize("Ren� T�est", Normalizer.Form.NFD).replaceAll("\\p{M}", ""));
        System.out.println(Word.flattenToAscii("Dit is een Test"));
        System.out.println(Word.flattenToAscii("D�t is Ren� Zijn T:est"));


    }


    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }
}


