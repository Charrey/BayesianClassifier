package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
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
            return new Word(word, newWordMap);
        }
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
