package test;

import main.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by René on 8-12-2015.
 */
public class ClasificationTest {

    public static final boolean deleteSubject = true;

    public static HashMap<String, List<Word>> featureList;

    public static void main(String[] args) {
        featureList = FeatureSelector.getChiWordList(DataManager2.INSTANCE.getWordList());

        List<String> classes = DataManager2.INSTANCE.getClasses();
        HashMap<String, Integer[]> resultMap = new HashMap<>();
        for(String c:classes){
            resultMap.put(c, new Integer[]{0,0});
        }

        String path = "blogs_test";
        File root = new File(path);
        File[] inroot = root.listFiles();
        if(!Builder2.checkMapFormat(inroot)){
            throw new IllegalStateException("Incorrect Format");
        }
        int[] total = new int[2];
        for(int i=0; i<inroot.length; i++){
            if(inroot[i].isDirectory()){
                String className = inroot[i].getName();
                processMap(inroot[i], className, resultMap);
            }
        }
        printResult(resultMap);
    }

    private static void printResult(HashMap<String, Integer[]> resultMap) {
        int[] total = new int[]{0,0};
        for(String c:resultMap.keySet()){
            Integer[] result = resultMap.get(c);
            System.out.println("Class: "+c+" correct: "+result[0]+" incorrect: "+result[1]);
            total[0] += result[0];
            total[1] += result[1];
        }
        System.out.println("Total correct: "+total[0]+" incorrect: "+total[1]);
    }

    public static void processMap(File map, String c, HashMap<String, Integer[]> resultMap){
        int correct = 0;
        int incorrect = 0;
        File[] files = map.listFiles();
        for(int i = 0; i<files.length; i++){
//            if(procesFile(files[i], c)){
//                correct ++;
//            }else{
//                incorrect ++;
//            }
            procesFile(files[i], c, resultMap);
        }
        //return new int[]{correct, incorrect};
    }

    /**
     *
     * @param file file need to be clasified
     * @param c class of the file
     * @return true if correct classified
     */
    public static void procesFile(File file, String c, HashMap<String, Integer[]> resultMap){
        String content = "";

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            content = new String(data, "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(deleteSubject){
            content = Builder2.removeWordSubject(content);
        }
//        return MathManager.getClassification(Word.sanitize(content)).equals(c); //old method
//        return MathManager.getClassification(MathManager.getProbSentence(Word.sanitize(content), featureList)).equals(c);
//        boolean result = MathManager.getClassification(Word.sanitize(content)).equals(c); //old method
        boolean result = MathManager.getClassification(MathManager.getProbSentence(Word.sanitize(content), featureList)).equals(c);
        putResultMap(c, resultMap, result);
    }

    private static void putResultMap(String c, HashMap<String, Integer[]> resultMap, boolean correct){
        Integer[] currentVal = resultMap.get(c);
        if(correct){
            currentVal[0] += 1;
        }else{
            currentVal[1] += 1;
        }
        resultMap.put(c, currentVal);
    }


    private static File getFileByName(File[] folder, String name) {
        for(int i=0; i<folder.length; i++){
            if(folder[i].getName().equals(name)){
                return folder[i];
            }
        }
        return null;
    }


}
