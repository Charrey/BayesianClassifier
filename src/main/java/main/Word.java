package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.HashMap;

/**
 * Created by Administrator on 25-11-2015.
 */
public class Word {

//    public int falsecount;
//    public int truecount;
    private String word;
//    private List<Integer> counts = new ArrayList<>();
    private HashMap<String, Integer> counts = new HashMap<>();

//
//    public main.Word(String word, int truecount, int falsecount) {
//        this.word = word;
//        this.truecount = truecount;
//        this.falsecount = falsecount;
//    }

    public Word(String word, HashMap<String, Integer> map){
        this.word = word;
        this.counts = map;
    }

    public int getCountOfClass(String c){
        return counts.get(c);
    }


    public void addCount(String c){
//        if(c){
//            truecount++;
//        }else{
//            falsecount++;
//        }
        counts.put(c, counts.get(c)+1);
    }

//    public String getXML(){
//        //return "<text="+word+"><truec="+truecount+"><falsec="+falsecount+">";
//        String string = "";
//        string+="<text="+word+">";
//        for(String s:counts.keySet()){
//            string+="<"+s+"="+counts.get(s)+">";
//        }
//        return string;
//    }

    public JSONObject getJSON(){
        JSONObject main = new JSONObject();
        main.put("text", word);
        JSONArray array = new JSONArray();
        for(String s : counts.keySet()){
            JSONObject c = new JSONObject();
            c.put("name", s);
            c.put("count", counts.get(s));
            array.put(c);
        }
        main.put("classes", array);
        return main;
    }

//    @Override
//    public String toString() {
//        return "<text="+word+"><truec="+truecount+"><falsec="+falsecount+">";
//    }

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


}

