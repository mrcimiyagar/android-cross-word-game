package kasper.android.cross_word.back.models.memory;

import java.io.Serializable;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public class Word implements Serializable {

    private int id;
    private String word;
    private String meaning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }
}
