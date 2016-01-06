package main;

import java.util.HashMap;

/**
 * Created by René Boschma on 28-12-2015.
 */
public class featureObject {

    private boolean useNonBayesChi, deleteStopWords;
    private int minimalOccurence, minimalDocumentOccurence, useChi;
    private double K;
    /**
     *
     * @param useChi if chi square featureselection needs to be used
     * @param minimalOccurence the minimal amount of times the words needs to occure in the trainingsset in order to
     *                         take into account.
     * @param minimalDocumentOccurence the minimal amount of documents the word needs to occur in order to take this
     *                                 word into account.
     */
    /**
     *
     * @param useNonBayesChi if own chi square featureselection needs to be used
     * @param minimalOccurence the minimal amount of times the words needs to occure in the trainingsset in order to
     *                         take into account.
     * @param minimalDocumentOccurence the minimal amount of documents the word needs to occur in order to take this
     *                                 word into account.
     * @param K smoothing value
     * @param useChi if bayes chi needs to be used.
     *               <0 for not using
     *               ==0 for using it with words with statistical significance at the 0.001 level.
     *               >0 for using the useChi highest amount of words per class. Per class usChi amount of words gets selected
     *                  and these will be used for determining the class.
     */
    public featureObject(boolean useNonBayesChi, int minimalOccurence, int minimalDocumentOccurence, double K, int useChi, boolean deleteStopWords){
        this.useNonBayesChi = useNonBayesChi;
        this.minimalOccurence = minimalOccurence;
        this.minimalDocumentOccurence = minimalDocumentOccurence;
        this.K = K;
        this.useChi = useChi;
        this.deleteStopWords = deleteStopWords;
    }

    public boolean deleteStopWords(){
        return deleteStopWords;
    }

    public boolean getUseNonBayesChi() {
        return useNonBayesChi;
    }

    public int getMinimalOccurence() {
        return minimalOccurence;
    }

    public int getMinimalDocumentOccurence() {
        return minimalDocumentOccurence;
    }

    public String getClassification(String[] document){
        HashMap<String, Word> map = DataManager2.INSTANCE.getWordList();
        FeatureSelector.removeUselessWords(map, minimalOccurence, minimalDocumentOccurence);
        if(useNonBayesChi){
            return MathManager.getClassification(MathManager.getProbSentence(document, FeatureSelector.getChiWordListOwnIdea(map), K));
        }else{
            if(useChi<0) {
                return MathManager.getClassification(MathManager.getProbSentenceFeatureList(document, map, K));
            }else if(useChi==0){
                return MathManager.getClassification(MathManager.getProbSentenceFeatureList(document, FeatureSelector.getChiWordList(map), K));
            }else{
                return MathManager.getClassification(MathManager.getProbSentenceFeatureList(document, FeatureSelector.getChiWordList(map, useChi), K));

            }
        }
    }
}
