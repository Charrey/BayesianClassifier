package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by René Boschma on 21-12-2015.
 */
public class DataManager2 {

    public static DataManager2 INSTANCE = new DataManager2();

    private HashMap<String, Word> words = new HashMap<String, Word>();
    private HashMap<String, Integer> wordcountPerClass = new HashMap<>();
    private HashMap<String, Integer> classCount = new HashMap<>();
    private HashMap<String, Integer> newWordMap = new HashMap<>();

    private int totalDocumentCount = 0;
    private int totalWordCount = 0;


    public DataManager2(){
        File data = new File("output"+File.separator+"data.xml");
        File meta = new File("output"+File.separator+"meta.xml");
        if(!(data.exists()&&meta.exists())){
            throw new IllegalStateException("data.xml or/and meta.xml not present");
        }
        processMeta(meta);
        processData(data);
        INSTANCE = this;
    }

    private void processData(File data){
        JSONArray array = new JSONArray(readFile(data));
        for(int i=0; i<array.length(); i++){
            JSONObject word = (JSONObject) array.get(i);
            String text = word.getString("text");
            JSONArray classes = word.getJSONArray("classes");
            HashMap<String, Integer> map = new HashMap<>();
            HashMap<String, Integer> doccountMap = new HashMap<>();
            for(int x=0; x<classes.length(); x++){
                JSONObject obj = classes.getJSONObject(x);
                String className = obj.getString("name");
                map.put(className, obj.getInt("count"));
                totalWordCount += obj.getInt("count");
                doccountMap.put(className, obj.getInt("doccount"));
                wordcountPerClass.put(className, wordcountPerClass.get(className)+obj.getInt("count"));
            }
            words.put(text, new Word(text, map, doccountMap));
        }
    }

    private void processMeta(File meta){
        JSONObject object = new JSONObject(readFile(meta));
        totalDocumentCount = object.getInt("totalDocumentCount");
        JSONArray array = object.getJSONArray("classes");
        for(int i=0; i<array.length(); i++){
            JSONObject c = array.getJSONObject(i);
            classCount.put(c.getString("name"), c.getInt("count"));
            wordcountPerClass.put(c.getString("name"), 0);
            newWordMap.put(c.getString("name"), 0);
        }
    }

    public static String readFile(File file){
        String content = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                content += line + " ";
                //System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public Word getWord(String word){
        Word w = words.get(word);
        if(w!=null){
            return w;
        }else{
            return new Word(word, getNewNewWordMap());
        }
    }

    /**
     * The feedback element in the classifier.
     * @param document the document needed to be added to the trainingsset
     * @param c the corresponding class of the docuement
     * @param writeToDisk if the trainingsset needs to be written back to the output files.
     */
    public void addDocumentToTrainingsset(String[] document, String c, boolean writeToDisk){
        for(int i=0; i<document.length; i++){
            Word w = words.get(document[i]);
            if(w==null){
                w = new Word(document[i], getNewNewWordMap());
            }
            w.addCount(c);
            w.addDocCount(c);
        }
        wordcountPerClass.put(c, wordcountPerClass.get(c)+document.length);
        classCount.put(c, classCount.get(c)+1);
        if(writeToDisk){
            Builder2.Build(words, classCount, totalDocumentCount);
        }
    }

    /**
     * function that looks stupid and exessive, but this takes care of the fact that java works with pointers to objects
     * if you assign a new object (no new object is created, what is this case should happen)
     * @return the new newWordMap containing all the classes with value 0.
     */
    private HashMap<String, Integer> getNewNewWordMap(){
        HashMap<String, Integer> map = new HashMap<>();
        map.putAll(newWordMap);
        return map;
    }

    public int getWordcountClass(String c){
        return wordcountPerClass.get(c);
    }

    public int getClassCount(String c){
        return classCount.get(c);
    }

    public int getClassCountExceptClass(String c){
        return getTotalDocumentCount() - getClassCount(c);
    }

    public int getTotalWordCount(){
        return totalWordCount;
    }

    public int getTotalDocumentCount(){
        return totalDocumentCount;
    }

    public HashMap<String, Word> getWordList(){
        return words;
    }

    public List<String> getClasses(){
        ArrayList<String> classes = new ArrayList<>();
        classes.addAll(wordcountPerClass.keySet());
        return classes;
    }
}
