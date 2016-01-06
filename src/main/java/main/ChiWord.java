package main;

/**
 * Created by René Boschma on 5-1-2016.
 */
public class ChiWord implements Comparable<ChiWord>{

    private Word w;
    private double chiScore;

    public ChiWord(Word w, double chiScore){
        this.w=w;
        this.chiScore = chiScore;
    }

    public Word getWord() {
        return w;
    }

    public double getChiScore() {
        return chiScore;
    }

    @Override
    public int compareTo(ChiWord o) {
        double dif = o.getChiScore()-chiScore;
        if(dif<0){
            return -1;
        }else if(dif>0){
            return 1;
        }else{
            return 0;
        }
    }
}
