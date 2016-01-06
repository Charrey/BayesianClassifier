package main;

import java.util.*;

/**
 * Created by René Boschma on 22-12-2015.
 */
public class FeatureSelector {

    /**
     *
     * @param originalList map of the wordlist (string value of the Word and corresponding Word object as value).
     * @param minnimumOccurence minimal occurences a word needs to have in the trainingsset, -1 if needed to be neglected
     * @param minimumDocumentOccurence minimal count of documents a word needs occur in the trainingsset, -1 if needed to be neglected
     * @return what is left of the original list after the filtering.
     */
    public static HashMap<String, Word> removeUselessWords(HashMap<String, Word> originalList, int minnimumOccurence, int minimumDocumentOccurence){
        HashMap<String, Word> result = new HashMap<>();
        result.putAll(originalList);
        for(String s:originalList.keySet()){
            if(!originalList.get(s).minimalOccurrence(minnimumOccurence)||!originalList.get(s).minimalDocOccurrece(minimumDocumentOccurence)){
                result.remove(s);
            }
        }
        return result;
    }

    /**
     *
     * @param originalList all words in the trainingsset with the text as key and word object as value
     * @return list of all words per class which need to be taken into account acording to the chi2 test.
     */
    public static HashMap<String, List<Word>> getChiWordListOwnIdea(HashMap<String, Word> originalList){
        List<String> classes = DataManager2.INSTANCE.getClasses();
        DataManager2 manager = DataManager2.INSTANCE;
        HashMap<String, List<Word>> result = new HashMap<>();
        HashSet<Word> amounntWordsSet = new HashSet<>();
        for(String c:classes){
            ArrayList<Word> words = new ArrayList<>();
            int totalDocumentCount = manager.getTotalDocumentCount();
            for(Word w:originalList.values()){
                /*
                N = totaal
                N00 = aantal documenten waarin term t niet voorkomt en niet behoort tot de klasse c
                N01 = aantal documenten waarin term t niet voorkomt en wel behoort tot de klasse c
                N10 = aantal documeten waarin term t wel voorkomt en niet behoort tot de klasse c
                N11 = aantal docunten waarin term t wel voorkomt en wel behoort tot de klasse c

                chi2 = N(N11*N00-N10*N01)^2 / ((N11+N01)(N11+N10)(N10+N00)(N01+N00))

                Source: http://blog.datumbox.com/using-feature-selection-methods-in-text-classification/
                */
                int N10 = w.getDocCountNotInClass(c);
                int N11 = w.getDocCountOfClass(c);
                int N00 = manager.getClassCountExceptClass(c) - N10;
                int N01 = manager.getClassCount(c) - N11;
                double chi2 = (double)totalDocumentCount*Math.pow(N11 * N00 - N10 * N01, 2) / ((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00));
                System.out.println(chi2);
                if(chi2>10.83){
                    //words.add(w);
                    if(w.getCountOfClass(c)==0){
                        //System.err.println(w.getJSON());
                    }else{
                        words.add(w);
                    }
                    //System.err.println("Word found with high enough chi: "+c+" "+w.getWord()+" Chi2: "+chi2);
                }
            }
            result.put(c, words);
            amounntWordsSet.addAll(words);
        }
//        System.out.println("amount of words removed: "+(originalList.values().size()-amounntWordsSet.size()));
        return result;
    }


    public static HashMap<String, Word> getChiWordList(HashMap<String, Word> originalList, int n){
        List<String> classes = DataManager2.INSTANCE.getClasses();
        DataManager2 manager = DataManager2.INSTANCE;
        HashMap<String, Word> result = new HashMap<>();
        HashSet<ChiWord> allSelectedWords = new HashSet<>();
        for(String c:classes){
            TreeSet<ChiWord> words = new TreeSet<>();
            int totalDocumentCount = manager.getTotalDocumentCount();
            for(Word w:originalList.values()){
                /*
                N = totaal
                N00 = aantal documenten waarin term t niet voorkomt en niet behoort tot de klasse c
                N01 = aantal documenten waarin term t niet voorkomt en wel behoort tot de klasse c
                N10 = aantal documeten waarin term t wel voorkomt en niet behoort tot de klasse c
                N11 = aantal docunten waarin term t wel voorkomt en wel behoort tot de klasse c

                chi2 = N(N11*N00-N10*N01)^2 / ((N11+N01)(N11+N10)(N10+N00)(N01+N00))

                Source: http://blog.datumbox.com/using-feature-selection-methods-in-text-classification/
                */
                int N10 = w.getDocCountNotInClass(c);
                int N11 = w.getDocCountOfClass(c);
                int N00 = manager.getClassCountExceptClass(c) - N10;
                int N01 = manager.getClassCount(c) - N11;
                double chi2 = (double)totalDocumentCount*Math.pow(N11 * N00 - N10 * N01, 2) / ((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00));

                if(words.size()<n){
                    words.add(new ChiWord(w, chi2));
                }else{
                    ChiWord last = words.last();
                    if(chi2>last.getChiScore()){
                        words.remove(last);
                        words.add(new ChiWord(w, chi2));
                    }
                }
            }
            allSelectedWords.addAll(words);
        }
        for(ChiWord chiWord:allSelectedWords){
            Word w = chiWord.getWord();
            result.put(w.getWord(), w);
        }
//        System.out.println("amount of words removed: "+(originalList.values().size()-amounntWordsSet.size()));
        return result;
    }

    public static HashMap<String, Word> getChiWordList(HashMap<String, Word> originalList){
        List<String> classes = DataManager2.INSTANCE.getClasses();
        DataManager2 manager = DataManager2.INSTANCE;
        HashMap<String, Word> result = new HashMap<>();
        HashSet<Word> allSelectedWords = new HashSet<>();
        for(String c:classes){
            int totalDocumentCount = manager.getTotalDocumentCount();
            for(Word w:originalList.values()){
                /*
                N = totaal
                N00 = aantal documenten waarin term t niet voorkomt en niet behoort tot de klasse c
                N01 = aantal documenten waarin term t niet voorkomt en wel behoort tot de klasse c
                N10 = aantal documeten waarin term t wel voorkomt en niet behoort tot de klasse c
                N11 = aantal docunten waarin term t wel voorkomt en wel behoort tot de klasse c

                chi2 = N(N11*N00-N10*N01)^2 / ((N11+N01)(N11+N10)(N10+N00)(N01+N00))

                Source: http://blog.datumbox.com/using-feature-selection-methods-in-text-classification/
                */
                int N10 = w.getDocCountNotInClass(c);
                int N11 = w.getDocCountOfClass(c);
                int N00 = manager.getClassCountExceptClass(c) - N10;
                int N01 = manager.getClassCount(c) - N11;
                double chi2 = (double)totalDocumentCount*Math.pow(N11 * N00 - N10 * N01, 2) / ((N11+N01)*(N11+N10)*(N10+N00)*(N01+N00));

                if(chi2>10.83){
                    allSelectedWords.add(w);
                }
            }
        }
        for(Word w:allSelectedWords){
            result.put(w.getWord(), w);
        }
//        System.out.println("amount of words removed: "+(originalList.values().size()-amounntWordsSet.size()));
        return result;
    }


    
}
