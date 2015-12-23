package main;

import java.util.Arrays;
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
        //printChances(result);
        return result;
    }

    /**
     * Functions that returns true if a given string belongs to the trained set
     * @param s the document needed to clasify in array format. Each word in sentence has own position
     * @return true if given srings belongs to the class, otherwise false
     */
    public static String getClassification(String[] s){
        //HashMap<String, Double> prob = getTrueProbSentece(s);
        return getClassification(getTrueProbSentece(s));
        /*
        String highestClass = prob.keySet().iterator().next();
        double highestProb = prob.get(highestClass);
        for(String c:prob.keySet()){
            if(prob.get(c)>highestProb){
                highestClass = c;
                highestProb = prob.get(c);
            }
        }
        return highestClass;
        */
    }

    /**
     *
     * @param prob hashmap containing the names of the classes as key and the probability of that class as value
     * @return the name of the class which has the highest probabilitie.
     */
    public static String getClassification(HashMap<String, Double> prob){
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

    public static double getProbWordGivenClass(Word w, int sentenceLength, String c, DataManager2 manager){
        return ((double)w.getCountOfClass(c) + K)/(manager.getWordcountClass(c)+K*sentenceLength);
    }

    public static double getProbClass(String c, DataManager2 manager){
        return (double) manager.getClassCount(c) / manager.getTotalDocumentCount();
    }

    public static HashMap<String, Double> getProbWordGivenClass(Word w, int sentenceLength, List<String> classes, DataManager2 manager){
        HashMap<String, Double> result = new HashMap<>();
        for(int i = 0; i<classes.size(); i++){
            String c = classes.get(i);
//            System.out.println("count class: "+w.getCountOfClass(c)+" wordcount class: "+manager.getWordcountClass(c)+" sentencelength: "+sentenceLength);
            //double chance = ((double)w.getCountOfClass(c) + K)/(manager.getWordcountClass(c)+K*sentenceLength);
            result.put(c, getProbWordGivenClass(w, sentenceLength, c, manager));
        }
        return result;
    }

    public static HashMap<String, Double> getProbWordGivenClass(String s, int sentenceLength, List<String> classes, DataManager2 manager){
        return getProbWordGivenClass(manager.getWord(s), sentenceLength, classes, manager);
    }

    public static HashMap<String, Double> getProbSentence(String[] s, HashMap<String, List<Word>> featuresPerClass){
        HashMap<String, Double> result = new HashMap<>();
        List<String> classes = DataManager2.INSTANCE.getClasses();
        DataManager2 manager = DataManager2.INSTANCE;
        List<String> sentenceList = Arrays.asList(s);
        int length = s.length;


        for(String c :classes){
            List<Word> featureList = featuresPerClass.get(c);
            double probWords = 0; // = P(Words)
            int count = 0;
            for(Word word : featureList){
                // calculating P(Words|C=c) for each word in the document and the featureset per class
                if(sentenceList.contains(word.getWord())){
                    double probWordGivenClass = getProbWordGivenClass(word, length, c, manager);
                    if(probWordGivenClass==0){
                        System.err.println("Prob of word: "+word.getWord()+" in class: "+c+" is zero "+word.getJSON());
                    }
                    if(result.get(c)!=null) {
                        result.put(c, result.get(c) + Math.log(probWordGivenClass));
                    }else{
                        result.put(c, Math.log(probWordGivenClass));
                    }
                    count++;
                    double probWord = getProbWord(word, manager);
//                System.out.println(probWord);
                    probWords += Math.log(probWord);
                }

            }
//            System.out.println("Class: "+c+" amount of words in feature and document: "+count);
            //calculating P(Words|C=c)/P(Words)
//            System.out.println("Prob of words is: "+probWords+ " prob of words given class is: "+result.get(c));
            result.put(c, result.get(c)-probWords);
            //calculating P(C=c)*P(Words|C=c)/P(Words)
            double probClass = getProbClass(c, manager);
            //System.out.println("prob of class: "+c+" is:"+Math.log(probClass));
            result.put(c, result.get(c) + Math.log(probClass));
        }
//        printChances(result);
        return result;
    }

    public static double getProbWord(Word w, DataManager2 manager){
        return (double)w.getTotalCount()/manager.getTotalWordCount();
    }

    private static void printChances(HashMap<String, Double> map){
        for(String c:map.keySet()){
            System.out.println("Class: " + c + " chance: " + map.get(c));
        }
    }

}
