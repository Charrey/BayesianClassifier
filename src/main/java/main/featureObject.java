package main;

import java.util.HashMap;

/**
 * Created by René Boschma on 28-12-2015.
 */
public class featureObject {

    private boolean useChi;
    private int minimalOccurence, minimalDocumentOccurence;
    private double K;
    /**
     *
     * @param useChi if chi square featureselection needs to be used
     * @param minimalOccurence the minimal amount of times the words needs to occure in the trainingsset in order to
     *                         take into account.
     * @param minimalDocumentOccurence the minimal amount of documents the word needs to occur in order to take this
     *                                 word into account.
     */
    public featureObject(boolean useChi, int minimalOccurence, int minimalDocumentOccurence, double K){
        this.useChi = useChi;
        this.minimalOccurence = minimalOccurence;
        this.minimalDocumentOccurence = minimalDocumentOccurence;
        this.K = K;
    }

    public boolean getUseChi() {
        return useChi;
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
        if(useChi){
            return MathManager.getClassification(MathManager.getProbSentence(document, FeatureSelector.getChiWordList(map), K));
        }else{
            return MathManager.getClassification(MathManager.getProbSentenceFeatureList(document, map, K));
        }
    }
}
