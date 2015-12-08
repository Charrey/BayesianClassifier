package main;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

/**
 * Created by Administrator on 25-11-2015.
 */
public final class DataManager {

    public static List<Word> words = new LinkedList<Word>();
    public static final boolean initialized = initialize();

    private static int totalFalseWordcount = 0, totalTrueWordcount = 0;

    private static boolean initialize() {
        //File file = new File("data.xml");
        File file = new File("output"+File.separator+"data.xml");
        System.err.println("data file opent: "+file.getAbsolutePath());
        if (!file.exists()) {
            System.err.println("data.xml not present");
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.err.println("I created it for you ;)");
            return false;
        }

        //File meta = new File("meta.xml");
        File meta = new File("output"+File.separator+"meta.xml");
        if (!meta.exists()) {
            System.err.println("meta.xml not present");
            try {
                meta.createNewFile();
                PrintWriter writer = new PrintWriter("meta.xml", "UTF-8");
                writer.print("<instancecount=0><truecount=0>");
                totalinstancecount = 0;
                totaltrueidentitycount = 0;
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            System.err.println("I created it for you ;)");
            return false;
        } else {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(meta);
            } catch (FileNotFoundException e) {
                //Can't happen
                e.printStackTrace();
            }
            byte[] data = new byte[(int) meta.length()];
            try {
                fis.read(data);

                  fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String str = null;
            try {
                str = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                //Can't happen
                e.printStackTrace();
            }
            int index = str.indexOf("<instancecount=")+"<instancecount=".length();
            int end = str.indexOf(">");
            totalinstancecount = Integer.parseInt(str.substring(index, end));
            index = str.indexOf("<truecount=", end)+"<truecount=".length();
            end = str.indexOf(">", index);
            totaltrueidentitycount = Integer.parseInt(str.substring(index, end));
            System.err.println("totalcount document: "+totalinstancecount+" totalcount true document: "+totaltrueidentitycount);
        }


        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Can't happen
        }

        String line = null;
        try {
        while ((line = br.readLine()) != null) {

            int index = line.indexOf("<text=")+"<text=".length();
            int end = line.indexOf(">", index);
            String string = line.substring(index, end);

            index = line.indexOf("<truec=", end)+"<truec=".length();
            end = line.indexOf(">", index);
            int truecount = Integer.parseInt(line.substring(index, end));


            index = line.indexOf("<falsec=", end)+"<falsec=".length();
            end = line.indexOf(">", index);

            int falsecount = Integer.parseInt(line.substring(index, end));

            Word word = new Word(string, truecount, falsecount);
            if (string==null) {
                System.err.println("data.xml corrupt, shutting down");
                System.exit(1);
            }

            add(word);

        }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(NumberFormatException e1) {
            System.exit(1);
            System.err.println("Data.xml corrupt..");
        }


        return true;
    }


    public static int totalinstancecount;
    public static int totaltrueidentitycount;




    public static void add(Set<Word> wordset) {
        for (Word i : wordset) {
            add(i);
        }
    }

    public static void add(Word word) {
        if (words.contains(word)) {
            throw new IllegalStateException("Word already present!");
        } else {
            totalTrueWordcount += word.truecount;
            totalFalseWordcount += word.falsecount;
            words.add(word);
        }
    }

    public static Word getWord(String input) {
       if (input.contains(" ")) {
           throw new IllegalArgumentException("Entered multiple words!");
       }

        for (Word i : words) {
            if (i.word.equals(Word.sanitize(input)[0])) {
                return i;
            }
        }
        Word word = new Word(Word.sanitize(input)[0], 0, 0);
        return word;
    }

    public static int getTotalWordCount() {
        return totalinstancecount;
    }

    public static void updateWords(String string, boolean istrue) {
        for (Word i : words) {
            if (i.word.equals(Word.sanitize(string)[0])) {
                if (istrue) {
                    i.truecount++;
                } else {
                    i.falsecount++;
                }
                break;
            }
        }
    }

    public static Set<Word> getAllWords() {
        return new HashSet<Word>(words);
    }

    public static void saveToDisk() {
        File meta = new File("meta.xml");
        try {
            FileWriter wr = new FileWriter(meta);
            wr.write("<instancecount="+totalinstancecount+"><truecount="+totaltrueidentitycount+">");
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(words, new WordComparator());

        File data = new File("data.xml");
        try {
            FileWriter wr = new FileWriter(data);
            for (Word I : words) {
                wr.write(I.toString()+"\n");
            }
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static int getWordcountTrue(){
        //TODO return total wordcount in true (so not unique words)
        return totalTrueWordcount;
    }

    public static int getWordcountFalse(){
        //TODO return total wordcount in false (so not unique words)
        return totalFalseWordcount;
    }

    public static int getTotalDocumentCount(){
        //TODO return total documents in trainingsset.
        return totalinstancecount;
    }

    public static int getTotalDocumentTrueCount(){
        //TODO return total document true to class in trainingset
        return totaltrueidentitycount;
    }



}
