package test;

import main.*;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by René on 8-12-2015.
 */
public class ClasificationTest {

    public static final boolean deleteSubject = true;


    public static void main(String[] args) {
        featureObject noFeatureK1 = new featureObject(false, -1, -1, 1, -1, false);
        featureObject noFeatureK1DeleteStop = new featureObject(false, -1, -1, 1, -1, true);
        featureObject noFeatureKhalf = new featureObject(false, -1, -1, 0.5, -1, false);
        featureObject removeUncommon = new featureObject(false, 2, 2, 1, -1, false);
        featureObject useChi = new featureObject(false, -1, -1, 1, 0, false);
        featureObject useChiRemoveUncommon = new featureObject(false, 2, 2, 1, 0, false);
        featureObject useChiWithN1000 = new featureObject(false, 2, 2, 1, 1000, false);
        featureObject useChiWithN500 = new featureObject(false, 2, 2, 1, 500, false);
        featureObject useOwnChi = new featureObject(true, 2, 2, 1, 0, false);

        List<String> toDeleteMail = new ArrayList<>();
        toDeleteMail.add("Subject:");
        new Builder2("mail_bb", toDeleteMail);
        System.out.println("Results mail:");
        procesTestMap("mail_test", noFeatureK1);
        println();
        procesTestMap("mail_test", noFeatureK1DeleteStop);
        println();
        procesTestMap("mail_test", noFeatureKhalf);
        println();
        procesTestMap("mail_test", removeUncommon);
        println();
        procesTestMap("mail_test", useChi);
        println();
        procesTestMap("mail_test", useChiWithN1000);
        println();
        procesTestMap("mail_test", useChiWithN500);
        println();
        procesTestMap("mail_test", useChiRemoveUncommon);
        println();
        procesTestMap("mail_test", useOwnChi);

        System.out.println("");
        System.out.println("");
        new Builder2("blogs_bb", null);
        new DataManager2();
        System.out.println("Results blogs:");
        procesTestMap("blogs_test", noFeatureK1);
        println();
        procesTestMap("blogs_test", noFeatureK1DeleteStop);
        println();
        procesTestMap("blogs_test", noFeatureKhalf);
        println();
        procesTestMap("blogs_test", removeUncommon);
        println();
        procesTestMap("blogs_test", useChi);
        println();
        procesTestMap("blogs_test", useChiWithN1000);
        println();
        procesTestMap("blogs_test", useChiWithN500);
        println();
        procesTestMap("blogs_test", useChiRemoveUncommon);
        println();
        procesTestMap("blogs_test", useOwnChi);


    }

    private static void println(){
        System.out.println("-----------------------------------------");
    }

    private static void procesTestMap(String rootPath, featureObject featureobj){
        List<String> classes = DataManager2.INSTANCE.getClasses();
        HashMap<String, Integer[]> resultMap = new HashMap<>();
        for(String c:classes){
            resultMap.put(c, new Integer[]{0,0});
        }
        File root = new File(rootPath);
        File[] inroot = root.listFiles();
        if(!Builder2.checkMapFormat(inroot)){
            throw new IllegalStateException("Incorrect Format");
        }

        for(int i=0; i<inroot.length; i++){
            if(inroot[i].isDirectory()){
                String className = inroot[i].getName();
                processMap(inroot[i], className, resultMap, featureobj);
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

    public static void processMap(File map, String c, HashMap<String, Integer[]> resultMap, featureObject featureobj){
        File[] files = map.listFiles();
        for(int i = 0; i<files.length; i++){
            procesFile(files[i], c, resultMap, featureobj);
        }
    }

    /**
     *
     * @param file file need to be clasified
     * @param c class of the file
     * @return true if correct classified
     */
    public static void procesFile(File file, String c, HashMap<String, Integer[]> resultMap, featureObject featureobj){
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

//        return MathManager.getClassification(Word.sanitize(content)).equals(c); //old method
//        return MathManager.getClassification(MathManager.getProbSentence(Word.sanitize(content), featureList)).equals(c);
//        boolean result = MathManager.getClassification(Word.sanitize(content)).equals(c); //old method
        //boolean result = MathManager.getClassification(MathManager.getProbSentence(Word.sanitize(content), featureList, 1)).equals(c);
//        boolean result = featureobj.getClassification(Word.sanitize(content)).equals(c);
        boolean result = MathManager.getClassificationOfDocument(content, featureobj, null).equals(c);
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
