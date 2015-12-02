package main;

import java.text.Normalizer;
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

    public static String flattenToAscii(String string) {
        char[] out = new char[string.length()];
        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        int j = 0;
        for (int i = 0, n = string.length(); i < n; ++i) {
            char c = string.charAt(i);
            if (c <= '\u007F') out[j++] = c;
        }
        return new String(out);
    }

}

