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

        for(int i=0; i<length; i++){
            HashMap<String, Double> map = getProbWordGivenClass(s[i], length, classes, manager);
            for(int x=0;x<classes.size(); x++){
                String c = classes.get(x);
                if(result.get(c)!=null) {
                    result.put(c, result.get(c) + Math.log(map.get(c)));
                }else{
                    result.put(c, Math.log(map.get(c)));
                }

            }
        }
        for(String c:classes){
            double chance = (double)manager.getClassCount(c)/manager.getTotalDocumentCount();
            result.put(c, result.get(c)+Math.log(chance));
        }
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
        HashMap<String, Double> result = new HashMap<>();
        for(int i = 0; i<classes.size(); i++){
            String c = classes.get(i);
            Word w = manager.getWord(s);
//            System.out.println("count class: "+w.getCountOfClass(c)+" wordcount class: "+manager.getWordcountClass(c)+" sentencelength: "+sentenceLength);
            double chance = ((double)w.getCountOfClass(c) + K)/(manager.getWordcountClass(c)+K*sentenceLength);
            result.put(c, chance);
        }
        return result;
    }

    private static void printChances(HashMap<String, Double> map){
        for(String c:map.keySet()){
            System.out.println("Class: " + c + " chance: " + map.get(c));
        }
    }

}
