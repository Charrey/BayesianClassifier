package main.test;

import main.Builder2;
import main.MathManager;
import main.Word;

import java.io.*;

/**
 * Created by René on 8-12-2015.
 */
public class ClasificationTest {

    public static final boolean deleteSubject = true;

    public static void main(String[] args) throws Exception {
        String path = "test";
        File root = new File(path);
        File[] inroot = root.listFiles();
        if(!Builder2.checkMapFormat(inroot)){
            throw new Exception("Incorrect Format");
        }
        File mapC = getFileByName(inroot, "c");
        File mapnC = getFileByName(inroot, "nc");
        System.out.println("Processing first map");
        int[] C = processMap(mapC, true);
        System.out.println("Processing second map");
        int[] nC = processMap(mapnC, false);
        int[] total = new int[]{C[0]+nC[0], C[1]+nC[1]};
        System.out.println("Total correct: "+total[0]+" total incorrect: "+total[1]);
        System.out.println("Total C correct: "+C[0]+" total C incorrect: "+C[1]);
        System.out.println("Total nC correct: "+nC[0]+" total nC incorrect: "+nC[1]);
    }

    public static int[] processMap(File map, boolean c){
        int correct = 0;
        int incorrect = 0;
        File[] files = map.listFiles();
        for(int i = 0; i<files.length; i++){
            if(procesFile(files[i], c)){
                correct ++;
            }else{
                incorrect ++;
            }
        }
        return new int[]{correct, incorrect};
    }

    /**
     *
     * @param file file need to be clasified
     * @param c of the file
     * @return true if correct classified
     */
    public static boolean procesFile(File file, boolean c){
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

        /*
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null) {
                content += line + " ";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        if(deleteSubject){
            content = Builder2.removeWordSubject(content);
        }
        return MathManager.getClassification(Word.sanitize(content)) == c;
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
