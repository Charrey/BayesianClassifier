package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 27-11-2015.
 */
public final class MathManager {

    //private static final double K = 1;

    /**
     * THE function of getting the classification of a document. This function also gives the option to use featureselection.
     * @param document the document needed to be classified
     * @param obj the featureobject, @see featureObject for further details
     * @param toIgnore a list of words need to be ignored in the document (NOTION: words are unsanitized).
     * @return the class according to the classifier.
     */
    public static String getClassificationOfDocument(String document, featureObject obj, List<String> toIgnore){
        String[] array;
        if(obj.deleteStopWords()){
            String doc = deleteWordsFromArray(Word.sanitizeString(document), toIgnore);
            array = deleteWordsFromArray(doc, FeatureSelector.STOPWORDS).split("\\s+");
        }else{
            array = deleteWordsFromArray(Word.sanitizeString(document), toIgnore).split("\\s+");
        }
        return obj.getClassification(array);
    }

    public static String deleteWordsFromArray(String doc, List<String> toDelete){
        String result = doc;
        for(String string:toDelete){
            String regex = "\\s*\\b"+string+"\\b\\s*";
            result = result.replaceAll(regex, "");
        }
        return result;
    }

    /**
     *
     * @param s the document needed to clasify in array format. Each word in sentence has own position
     * @return hashmap containing the name of the class as key and the probabilite as value.
     */
    public static HashMap<String, Double> getTrueProbSentece(String[] s, double K){
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
            HashMap<String, Double> map = getProbWordGivenClass(s[i], length, classes, manager, K);
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
    public static String getClassification(String[] s, double K){
        //HashMap<String, Double> prob = getTrueProbSentece(s);
        return getClassification(getTrueProbSentece(s, K));
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

    public static double getProbWordGivenClass(Word w, int sentenceLength, String c, DataManager2 manager, double K){
        double prob = ((double)w.getCountOfClass(c) + K)/(manager.getWordcountClass(c)+K*sentenceLength);
        if(prob<=0){
            System.err.println("Chance P(Word|C) is 0 or lower");
        }
        return prob;
    }

    public static double getProbClass(String c, DataManager2 manager){
        double prob = (double) manager.getClassCount(c) / manager.getTotalDocumentCount();
        if(prob<=0){
            System.err.println("Chance P(C) is 0 or lower");
        }
        return prob;
    }

    public static HashMap<String, Double> getProbWordGivenClass(Word w, int sentenceLength, List<String> classes, DataManager2 manager, double K){
        HashMap<String, Double> result = new HashMap<>();
        for(int i = 0; i<classes.size(); i++){
            String c = classes.get(i);
//            System.out.println("count class: "+w.getCountOfClass(c)+" wordcount class: "+manager.getWordcountClass(c)+" sentencelength: "+sentenceLength);
            //double chance = ((double)w.getCountOfClass(c) + K)/(manager.getWordcountClass(c)+K*sentenceLength);
            result.put(c, getProbWordGivenClass(w, sentenceLength, c, manager, K));
        }
        return result;
    }

    public static HashMap<String, Double> getProbWordGivenClass(String s, int sentenceLength, List<String> classes, DataManager2 manager, double K){
        return getProbWordGivenClass(manager.getWord(s), sentenceLength, classes, manager, K);
    }

    /**
     *
     * @param s the document needed to classify
     * @param featuresPerClass list of words needed to be looked at per class. This map can for example be generated by
     *                         using the chi2 method.
     * @return hashmap containing the name of the class as key and the probabilite as value.
     */
    public static HashMap<String, Double> getProbSentence(String[] s, HashMap<String, List<Word>> featuresPerClass, double K){
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
                    double probWordGivenClass = getProbWordGivenClass(word, length, c, manager, K);
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

    /**
     *
     * @param s the document need to be classified
     * @param featurueList the list of words that need to be used with the string value of the word as key and the word object
     *                     as value.
     * @return hashmap containing the name of the class as key and the probabilite as value.
     */
    public static HashMap<String, Double> getProbSentenceFeatureList(String[] s, HashMap<String, Word> featurueList, Double K){
        HashMap<String, List<Word>> map =  new HashMap<>();
        List<String> classes = DataManager2.INSTANCE.getClasses();
        List<Word> wordList = new ArrayList<>(featurueList.values());
        for(String c:classes){
            map.put(c, wordList);
        }
        return getProbSentence(s, map, K);
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
