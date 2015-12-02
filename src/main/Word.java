package main;

import java.util.Comparator;

/**
 * Created by Administrator on 25-11-2015.
 */
public class Word {

    public int falsecount;
    public int truecount;
    public String word;

    public Word(String word, int truecount, int falsecount) {
        this.word = word;
        this.truecount = truecount;
        this.falsecount = falsecount;
    }

    @Override
    public String toString() {
        return "<text="+word+"><truec="+truecount+"><falsec="+falsecount+">";
    }

    @Override
    public boolean equals(Object word) {
        if (word instanceof Word && ((Word) word).word.equals(word)) {
            return true;
        }
        return false;
    }

    public static String[] sanitize(String input) {
       return input.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
    }


}

