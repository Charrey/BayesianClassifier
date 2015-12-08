package main;


import java.util.Arrays;

/**
 * Created by Administrator on 27-11-2015.
 */
public final class MathManager {

    private static final double K = 1;


    /**
     *
     * @param s the document needed to clasify in array format. Each word in sentence has own position
     * @return [0] for the prob true [1] for prob false. Both are in log(Chance)
     */
    public static double[] getTrueProbSentece(String[] s){
        int length = s.length;
        /*
        Formule:
        probTrueGivenWords = P(C)*P(Words|C)/P(Words)
        probFalseGivenWords = P(!C)*P(Words|!C)/P(Words)

        P(Words) has the same value, so can be neglected
         */
        double probTrueGivenWords = 0;
        double probFalseGivenWords = 0;

        for(int i=0; i<length; i++){
            double[] wordGivenClass = getProbWordGivenClass(s[i], length);
            probTrueGivenWords += Math.log(wordGivenClass[0]);
            probFalseGivenWords += Math.log(wordGivenClass[1]);
        }
        double pC = (double)DataManager.getTotalDocumentTrueCount()/DataManager.getTotalDocumentCount();
        double pnC = (double)(DataManager.getTotalDocumentCount() - DataManager.getTotalDocumentTrueCount())/DataManager.getTotalDocumentCount();
        //probTrueGivenWords += Math.log(DataManager.getTotalDocumentTrueCount()/DataManager.getTotalDocumentCount());
        //probFalseGivenWords += Math.log((DataManager.getTotalDocumentCount() - DataManager.getTotalDocumentTrueCount())/DataManager.getTotalDocumentCount());
        probTrueGivenWords += Math.log(pC);
        probFalseGivenWords += Math.log(pnC);
        /*
        System.out.println("Sentence: \""+ Arrays.toString(s)+"\". chance true:"+probTrueGivenWords+" chance false: "+probFalseGivenWords);
        System.out.println("P(C): "+pC+" P(!C): "+pnC);
        System.out.println("totalcount document: "+DataManager.getTotalDocumentCount()+" totalcount true document: "+DataManager.getTotalDocumentTrueCount());
        */
        return new double[]{probTrueGivenWords, probFalseGivenWords};
    }

    /**
     * Functions that returns true if a given string belongs to the trained set
     * @param s the document needed to clasify in array format. Each word in sentence has own position
     * @return true if given srings belongs to the class, otherwise false
     */
    public static boolean getClassification(String[] s){
        double[] prob = getTrueProbSentece(s);
        return prob[0]>=prob[1];
    }

    public static double[] getProbWordGivenClass(String s, int sentenceLength){
        Word word = DataManager.getWord(s);
        //System.err.println("Word: "+s+" truecount:"+word.truecount+" falsecount: "+word.falsecount);
        double chanceTrue = (word.truecount + K)/(DataManager.getWordcountTrue()+K*sentenceLength);
        double chanceFalse = (word.falsecount + K)/(DataManager.getWordcountFalse()+K*sentenceLength);
        //System.out.println(s + " chance true: "+chanceTrue+" chance false: "+chanceFalse);
        return new double[]{chanceTrue, chanceFalse};
    }

}
