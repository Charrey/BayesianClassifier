package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.HashMap;

/**
 * Created by Administrator on 25-11-2015.
 */
public class Word {

    private String word;
    private HashMap<String, Integer> counts = new HashMap<>();
    private HashMap<String, Integer> doccounts = new HashMap<>();


    public Word(String word, HashMap<String, Integer> map){
        this.word = word;
        this.counts = map;
        for(String c:map.keySet()){
            doccounts.put(c, 0);
        }
    }
    public Word(String word, HashMap<String, Integer> map, HashMap<String, Integer> doccountMap){
        this.word = word;
        this.counts = map;
        this.doccounts = doccountMap;
    }

    public int getCountOfClass(String c){
        return counts.get(c);
    }

    public int getDocCountOfClass(String c){
        return doccounts.get(c);
    }

    /**
     *
     * @return total amount of documents where this word occurs
     */
    public int getTotalDocCount(){
        int result = 0;
        for(Integer i:doccounts.values()){
            result += i;
        }
        return result;
    }

    public int getTotalCount(){
        int result = 0;
        for(Integer i:counts.values()){
            result += i;
        }
        return result;
    }

    /**
     *
     * @param c class which needs to be neglected in the doccount
     * @return the amount of documents of not class c where this word occurs.
     */
    public int getDocCountNotInClass(String c){
        return getTotalDocCount()-getCountOfClass(c);
    }

    public void addCount(String c){
        counts.put(c, counts.get(c)+1);
    }

    public void addDocCount(String c){
        doccounts.put(c, doccounts.get(c)+1);
    }

    /**
     *
     * @return json object for the Word object. text string value of the word and contains an array containing per
     * class: the "name" of the class and the total amount of occurences in all documents "count" and "doccount" the
     * amount of documents the word occurs in.
     */
    public JSONObject getJSON(){
        JSONObject main = new JSONObject();
        main.put("text", word);
        JSONArray array = new JSONArray();
        for(String s : counts.keySet()){
            JSONObject c = new JSONObject();
            c.put("name", s);
            c.put("count", counts.get(s));
            c.put("doccount", doccounts.get(s));
            if(counts.get(s)<doccounts.get(s)){
                System.err.println("Word: " + word + " count: " + counts.get(s) + " doccount: " + doccounts.get(s));
            }
            array.put(c);
        }
        main.put("classes", array);
        return main;
    }


    @Override
    public boolean equals(Object word) {
        if (word instanceof Word && ((Word) word).word.equals(word)) {
            return true;
        }
        return false;
    }

    /*
    public static String[] removeNonLetters(String input) {
       return input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
    }*/

    public static String[] sanitize(String string) {
        // Funcion below thanks to David Conrad.
        char[] out = new char[string.length()];
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = string.length(); i < n; ++i) {
            char c = string.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        String toReturn = new String(out).replaceAll("[^a-zA-Z ]", "").toLowerCase(); //replaces non letters
        toReturn = toReturn.replaceAll("\\s+", " "); //deletes double spaces
        toReturn = toReturn.trim(); //delete leading and trailing spaces

        return toReturn.split("\\s+");
    }

    /**
     *
     * @param count minimal amount a word needs to be occured in the trainingsset
     * @return false if word does not occur often enough, otherwise true.
     */
    public boolean minimalOccurrence(int count){
        boolean result = false;
        for(Integer i:counts.values()){
            if(i>count){
                result = true;
            }
        }
        return result;
    }

    public boolean minimalDocOccurrece(int count){
        boolean result = false;
        for(Integer i:doccounts.values()){
            if(i>count){
                result = true;
            }
        }
        return result;
    }

    public String getWord(){
        return word;
    }


}

