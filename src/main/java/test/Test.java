package test;




import main.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        //FeatureSelector.getChiWordListOwnIdea(FeatureSelector.removeUselessWords(DataManager2.INSTANCE.getWordList(), 1));
        //FeatureSelector.getChiWordListOwnIdea(DataManager2.INSTANCE.getWordList());
//
//        HashMap<String, List<Word>> test = FeatureSelector.getChiWordListOwnIdea(DataManager2.INSTANCE.getWordList());
//        System.out.println("Words in trainignsset:" +DataManager2.INSTANCE.getWordList().size());
//        for(String c:test.keySet()){
//            for(String x:test.keySet()){
//                if(!c.equals(x)){
//                    System.out.println("list are equal: "+test.get(c).equals(test.get(x)));
//                    System.out.println(test.get(c).size() + " "+test.get(x).size());
//                }
//            }
//        }
        List<String> toDeleteMail = new ArrayList<>();
        toDeleteMail.add("Subject:");
        new Builder2("mail_bb", toDeleteMail);
        new DataManager2();
        FeatureSelector.getChiWordList(DataManager2.INSTANCE.getWordList());
    }


    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }

}


