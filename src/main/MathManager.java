package main;


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
    public double[] getTrueProbSentece(String[] s){
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
        probTrueGivenWords += Math.log(DataManager.getTotalDocumentTrueCount()/DataManager.getTotalDocumentCount());
        probFalseGivenWords += Math.log((DataManager.getTotalDocumentCount() - DataManager.getTotalDocumentTrueCount())/DataManager.getTotalDocumentCount());
        return new double[]{probTrueGivenWords, probFalseGivenWords};
    }

    public double[] getProbWordGivenClass(String s, int sentenceLength){
        Word word = DataManager.getWord(s);
        double chanceTrue = (word.truecount + K)/(DataManager.getWordcountTrue()+K*sentenceLength);
        double chanceFalse = (word.falsecount + K)/(DataManager.getWordcountFalse()+K*sentenceLength);
        return new double[]{chanceTrue, chanceFalse};
    }

}
