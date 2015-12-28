package main;

import java.util.HashMap;

/**
 * Created by René Boschma on 28-12-2015.
 */
public class featureObject {

    private boolean useChi;
    private int minimalOccurence, minimalDocumentOccurence;


    public featureObject(boolean useChi, int minimalOccurence, int minimalDocumentOccurence){
        this.useChi = useChi;
        this.minimalOccurence = minimalOccurence;
        this.minimalDocumentOccurence = minimalDocumentOccurence;
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

    public String getClassification(String[] document, double K){
        HashMap<String, Word> map = DataManager2.INSTANCE.getWordList();
        FeatureSelector.removeUselessWords(map, minimalOccurence, minimalDocumentOccurence);
        if(useChi){
            return MathManager.getClassification(MathManager.getProbSentence(document, FeatureSelector.getChiWordList(map), K));
        }else{
            return MathManager.getClassification(MathManager.getProbSentenceFeatureList(document, map, K));
        }
    }
}
