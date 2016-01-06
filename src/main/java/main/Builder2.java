package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by René Boschma on 7-12-2015.
 */
public class Builder2 {

    HashMap<String, Word> hashMap = new HashMap<String, Word>();
    public boolean deleteSubject = false;
    private List<String> toDelete;

    private int totalDocumentCount = 0;
    private HashMap<String, Integer> classCount = new HashMap<>();
    private HashMap<String, Integer> newWordMap = new HashMap<>();


    /**
     *
     * @param path to the folder containing the class folders which contain the training files
     * @param toBeDeleted list of words needed to be deleted before building the trainingsset. These words are unsanitized.
     *                    give null if no words need to be ignored.
     */
    public Builder2(String path, List<String> toBeDeleted) throws IllegalStateException{
        try {
            File outputFolder = new File("output");
            outputFolder.mkdir();
            File outputFile = new File(outputFolder, "data.xml");
            outputFile.createNewFile();

            File metaFile = new File(outputFolder, "meta.xml");
            metaFile.createNewFile();


            if(toBeDeleted!=null){
                deleteSubject = true;
                this.toDelete = toBeDeleted;
            }


            File root = new File(path);
            File[] inroot = root.listFiles();
            if(!checkMapFormat(inroot)){
                throw new IllegalStateException("Incorrect Format");
            }
            buildEmptyMaps(inroot);

            // Iterate all maps in root dir
            for(int i=0; i<inroot.length; i++){
                if(inroot[i].isDirectory()){
                    procesMap(inroot[i], inroot[i].getName());
                }
            }

            buildXML(outputFile, hashMap);
            buildMeta(metaFile, classCount, totalDocumentCount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * builds the files to the output folder
     * @param hashMap containing as key the string value of the Word and as value the coresponding Word object
     * @param classCount map containing the name of the class with as value the total document occurences of the class
     * @param totalDocumentCount total documents in the trainingsset
     */
    public static void Build(HashMap<String, Word> hashMap, HashMap<String, Integer> classCount, int totalDocumentCount){
        try {
            File outputFolder = new File("output");
            outputFolder.mkdir();
            File outputFile = new File(outputFolder, "data.xml");
            outputFile.createNewFile();
            File metaFile = new File(outputFolder, "meta.xml");
            metaFile.createNewFile();

            buildMeta(metaFile, classCount, totalDocumentCount);
            buildXML(outputFile, hashMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void buildEmptyMaps(File[] inroot){
        for(int i=0; i<inroot.length; i++){
            if(inroot[i].isDirectory()){
//                wordPerClassCount.put(inroot[i].getName(), 0);
                classCount.put(inroot[i].getName(), 0);
                newWordMap.put(inroot[i].getName(), 0);
            }
        }
    }

    public static void buildMeta(File metaFile,  HashMap<String, Integer> classCount, int totalDocumentCount) {
        PrintWriter writer = null;
        try {
            JSONObject obj = new JSONObject();
            obj.put("totalDocumentCount", totalDocumentCount);
            JSONArray array = new JSONArray();
            for(String string:classCount.keySet()){
                JSONObject c = new JSONObject();
                c.put("name", string);
                c.put("count", classCount.get(string));
                array.put(c);
            }
            obj.put("classes", array);
            writer = new PrintWriter(metaFile, "UTF-8");
            writer.println(obj);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void buildXML(File dataFile,  HashMap<String, Word> hashMap) {
        PrintWriter writer = null;
        try {
            //writer = new PrintWriter("data2.xml", "UTF-8");
            JSONArray array = new JSONArray();
            writer = new PrintWriter(dataFile, "UTF-8");
            for(Word w : hashMap.values()){
//                writer.println(w.getXML());
                array.put(w.getJSON());
            }
            writer.println(array);
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private File getFileByName(File[] folder, String name) {
        for(int i=0; i<folder.length; i++){
            if(folder[i].getName().equals(name)){
                return folder[i];
            }
        }
        return null;
    }

    public static boolean checkMapFormat(File[] map){
        int dirCount = 0;
        for(int i=0; i<map.length; i++){
            if(map[i].isDirectory()){
                dirCount++;
            }
        }
        return dirCount >= 2;
    }

    public void procesMap(File map, String c){
        File[] m = map.listFiles();
        for(int i = 0; i<m.length; i++){
            procesFile(m[i], c);
        }
    }

    public void procesFile(File file, String c){
        totalDocumentCount ++;
        classCount.put(c, classCount.get(c)+1);

        String content = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                content += line + " ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(deleteSubject){
            MathManager.deleteWordsFromArray(content, toDelete);
        }

        String[] array = Word.sanitize(content);
        HashSet<String> uniqueWords = new HashSet<>();
        for(int i =0; i<array.length; i++){
            Word w = hashMap.get(array[i]);
            uniqueWords.add(array[i]);
            if(w!=null){
                // Word is already in set
                w.addCount(c);
            }else{
                // Word not in set
                HashMap<String, Integer> newMap = new HashMap<>();
                newMap.putAll(newWordMap);
                Word newWord = new Word(array[i], newMap);
                newWord.addCount(c);
                hashMap.put(array[i], newWord);
                //System.out.println(newWord.getJSON());
            }
        }
        //System.err.println(uniqueWords);
        for(String unique:uniqueWords){
            hashMap.get(unique).addDocCount(c);
        }

    }

    public static String removeWordSubject(String content){
        return content.replace("Subject:", "");
    }

}
