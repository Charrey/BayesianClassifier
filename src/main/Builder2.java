package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by René Boschma on 7-12-2015.
 */
public class Builder2 {

    HashMap<String, Word> hashMap = new HashMap<String, Word>();
    public static final boolean deleteSubject = true;

    private int totalDocumentCount = 0, totaltrueDocumentCount = 0;


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
        File mapC = getFileByName(inroot, "c");
        File mapnC = getFileByName(inroot, "nc");
        procesMap(mapC, true);
        System.out.println("FIRST MAP DONE");
        procesMap(mapnC, false);
        System.out.println("Second MAP DONE");
        System.out.println("Writing to xml file");


        buildXML(outputFile);
        buildMeta(metaFile);


    }

    private void buildMeta(File metaFile) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(metaFile, "UTF-8");
            writer.println("<instancecount="+totalDocumentCount+"><truecount="+totaltrueDocumentCount+">");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void buildXML(File dataFile) {
        PrintWriter writer = null;
        try {
            //writer = new PrintWriter("data2.xml", "UTF-8");
            writer = new PrintWriter(dataFile, "UTF-8");
            for(Word w : hashMap.values()){
                writer.println(w.getXML());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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
        boolean c = false, nc = false;
        for(int i=0; i<map.length; i++){
            if(map[i].getName().equals("c")){
                c = true;
            }
            if(map[i].getName().equals("nc")){
                nc = true;
            }
        }
        return c && nc;
    }

    public void procesMap(File map, boolean c){
        File[] m = map.listFiles();
        for(int i = 0; i<m.length; i++){
            procesFile(m[i], c);
        }
    }

    public void procesFile(File file, boolean c){
        totalDocumentCount ++;

        if(c){
            totaltrueDocumentCount ++;
        }

        String content = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                content += line + " ";
                //System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(deleteSubject){
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
                if(c){
                    hashMap.put(array[i], new Word(array[i], 1, 0));
                }else{
                    hashMap.put(array[i], new Word(array[i], 0, 1));
                }
            }
        }

    }

    public static String removeWordSubject(String content){
        return content.replace("Subject:", "");
    }

}
