package main;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;

/**
 * Created by René Boschma on 7-12-2015.
 */
public class Builder2 {

    HashMap<String, Word> hashMap = new HashMap<String, Word>();
    public static final boolean deleteSubject = true;

    private int totalDocumentCount = 0;
//    private HashMap<String, Integer> wordPerClassCount = new HashMap<>();
    private HashMap<String, Integer> classCount = new HashMap<>();
    private HashMap<String, Integer> newWordMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
        new Builder2("data_bb");
    }

    public Builder2(String path) throws Exception {
        File outputFolder = new File("output");
        outputFolder.mkdir();
        File outputFile = new File(outputFolder, "data.xml");
        outputFile.createNewFile();
        File metaFile = new File(outputFolder, "meta.xml");
        metaFile.createNewFile();

        File root = new File(path);
        File[] inroot = root.listFiles();
        if(!checkMapFormat(inroot)){
            throw new Exception("Incorrect Format");
        }
        buildEmptyMaps(inroot);

        // Iterate all maps in root dir
        for(int i=0; i<inroot.length; i++){
            if(inroot[i].isDirectory()){
                procesMap(inroot[i], inroot[i].getName());
            }
        }
        //System.out.print(hashMap.values().iterator().next().getJSON());

//        File mapC = getFileByName(inroot, "c");
//        File mapnC = getFileByName(inroot, "nc");
//        procesMap(mapC, true);
//        System.out.println("FIRST MAP DONE");
//        procesMap(mapnC, false);
//        System.out.println("Second MAP DONE");
//        System.out.println("Writing to xml file");
//

        buildXML(outputFile);
        buildMeta(metaFile);


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

    private void buildMeta(File metaFile) {
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

    private void buildXML(File dataFile) {
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
//        boolean c = false, nc = false;
//        for(int i=0; i<map.length; i++){
//            if(map[i].getName().equals("c")){
//                c = true;
//            }
//            if(map[i].getName().equals("nc")){
//                nc = true;
//            }
//        }
//        return c && nc;
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

//        if(c){
//            totaltrueDocumentCount ++;
//        }

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

        if(deleteSubject){
            //TODO better overall function wich deletes multiple words if needed.
            content = removeWordSubject(content);
        }

        String[] array = Word.sanitize(content);
        for(int i =0; i<array.length; i++){
            Word w = hashMap.get(array[i]);

            if(w!=null){
                // Word is already in set
                w.addCount(c);
            }else{
                // Word is not yet in set
//                if(c){
//                    hashMap.put(array[i], new main.Word(array[i], 1, 0));
//                }else{
//                    hashMap.put(array[i], new main.Word(array[i], 0, 1));
//                }
                HashMap<String, Integer> newMap = new HashMap<>();
                newMap.putAll(newWordMap);
                Word newWord = new Word(array[i], newMap);
                hashMap.put(array[i], newWord);
                System.out.println(newWord.getJSON());
            }
        }

    }

    public static String removeWordSubject(String content){
        return content.replace("Subject:", "");
    }

}
