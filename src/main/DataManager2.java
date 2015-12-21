package main;

import java.io.File;
import java.util.HashMap;

/**
 * Created by René Boschma on 21-12-2015.
 */
public class DataManager2 {

    public static DataManager2 INSTANCE;

    private HashMap<String, Word> words = new HashMap<String, Word>();
    private HashMap<String, Integer> wordPerClassCount = new HashMap<>();
    private HashMap<String, Integer> classCount = new HashMap<>();

    private int totalDocumentCount = 0;


    public DataManager2(){
        File data = new File("output"+File.separator+"data.xml");
        File meta = new File("output"+File.separator+"meta.xml");
        if(!(data.exists()&&meta.exists())){
            throw new IllegalStateException("data.xml or/and meta.xml not present");
        }

        INSTANCE = this;
    }

    public Word getWord(String c){
        return words.get(c);
    }

    public int getWordCountPerClass(String c){
        return wordPerClassCount.get(c);
    }

    public int getClassCount(String c){
        return classCount.get(c);
    }

    public int getTotalDocumentCount(){
        return totalDocumentCount;
    }
}
