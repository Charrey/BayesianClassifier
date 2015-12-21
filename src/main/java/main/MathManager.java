package main;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 27-11-2015.
 */
public final class MathManager {

    private static final double K = 1;


    /**
     *
     * @param s the document needed to clasify in array format. Each word in sentence has own position
     * @return hashmap containing the name of the class as key and the probabilite as value.
     */
    public static HashMap<String, Double> getTrueProbSentece(String[] s){
        int length = s.length;
        /*
        Formule:
        probGivenWords = P(C=c)*P(Words|C=c)/P(Words)

        P(Words) has the same value, so can be neglected
         */

        List<String> classes = DataManager2.INSTANCE.getClasses();
        DataManager2 manager = DataManager2.INSTANCE;
        HashMap<String, Double> result = new HashMap<>();
//        System.out.println(classes);
//        for(String c:classes){
//            result.put(c, (double)0.0);
//            //System.out.println(c + " value putted, value of class is: " + result.get(c));
//        }

        //printChances(result);


        for(int i=0; i<length; i++){
//            double[] wordGivenClass = getProbWordGivenClass(s[i], length);
//            probTrueGivenWords += Math.log(wordGivenClass[0]);
//            probFalseGivenWords += Math.log(wordGivenClass[1]);
            HashMap<String, Double> map = getProbWordGivenClass(s[i], length, classes, manager);
            //for(String c:classes){
            for(int x=0;x<classes.size(); x++){
                //System.out.println("value in result map before: "+result.get(c)+" hashmap keys: "+result.keySet());
                String c = classes.get(x);
                //System.out.println(result.get(c));
                if(result.get(c)!=null) {
                    result.put(c, result.get(c) + Math.log(map.get(c)));
                }else{
                    //System.out.println(map.get(c));
                    result.put(c, Math.log(map.get(c)));
                }

            }
        }
        for(String c:classes){
            double chance = (double)manager.getClassCount(c)/manager.getTotalDocumentCount();
//            System.out.println("Class: " + c + " chance: " + chance + " classcount: " + manager.getClassCount(c) + " totalcount: chance: "+manager.getTotalDocumentCount());
//            System.out.println("key: "+c+" value: "+result.get(c));
            result.put(c, result.get(c)+Math.log(chance));

        }
        //printChances(result);
//        double pC = (double)DataManager.getTotalDocumentTrueCount()/DataManager.getTotalDocumentCount();
//        double pnC = (double)(DataManager.getTotalDocumentCount() - DataManager.getTotalDocumentTrueCount())/DataManager.getTotalDocumentCount();
        //probTrueGivenWords += Math.log(main.DataManager.getTotalDocumentTrueCount()/main.DataManager.getTotalDocumentCount());
        //probFalseGivenWords += Math.log((main.DataManager.getTotalDocumentCount() - main.DataManager.getTotalDocumentTrueCount())/main.DataManager.getTotalDocumentCount());
//        probTrueGivenWords += Math.log(pC);
//        probFalseGivenWords += Math.log(pnC);

        //System.out.println("Sentence: \""+ Arrays.toString(s)+"\". chance true:"+probTrueGivenWords+" chance false: "+probFalseGivenWords);
        //System.out.println("P(C): "+pC+" P(!C): "+pnC);
        //System.out.println("totalcount document: "+main.DataManager.getTotalDocumentCount()+" totalcount true document: "+main.DataManager.getTotalDocumentTrueCount());
//        printChances(result);
        return result;
    }

    /**
     * Functions that returns true if a given string belongs to the trained set
     * @param s the document needed to clasify in array format. Each word in sentence has own position
     * @return true if given srings belongs to the class, otherwise false
     */
    public static String getClassification(String[] s){
        HashMap<String, Double> prob = getTrueProbSentece(s);
        String highestClass = prob.keySet().iterator().next();
        double highestProb = prob.get(highestClass);
        for(String c:prob.keySet()){
            if(prob.get(c)>highestProb){
                highestClass = c;
                highestProb = prob.get(c);
            }
        }
        return highestClass;
    }

    public static HashMap<String, Double> getProbWordGivenClass(String s, int sentenceLength, List<String> classes, DataManager2 manager){
        //Word word = DataManager.getWord(s);
        //System.out.println("main.Word: "+s+" truecount:"+word.truecount+" falsecount: "+word.falsecount+" WordCountTrue: "+main.DataManager.getWordcountTrue()+" WordCountFalse: "+main.DataManager.getWordcountFalse());

//        double chanceTrue = ((double)word.truecount + K)/(DataManager.getWordcountTrue()+K*sentenceLength);
//        double chanceFalse = ((double)word.falsecount + K)/(DataManager.getWordcountFalse()+K*sentenceLength);
        //System.out.println(s + " chance true: "+chanceTrue+" chance false: "+chanceFalse);

        //return new double[]{chanceTrue, chanceFalse};
        HashMap<String, Double> result = new HashMap<>();
        for(int i = 0; i<classes.size(); i++){
            String c = classes.get(i);
            Word w = manager.getWord(s);
            System.out.println("count class: "+w.getCountOfClass(c)+" wordcount class: "+manager.getWordcountClass(c)+" sentencelength: "+sentenceLength);
            double chance = ((double)w.getCountOfClass(c) + K)/(manager.getWordcountClass(c)+K*sentenceLength);
            result.put(c, chance);
        }
        //printChances(result);
        return result;
    }

    private static void printChances(HashMap<String, Double> map){
        for(String c:map.keySet()){
            System.out.println("Class: " + c + " chance: " + map.get(c));
        }
    }

}
