package kasper.android.cross_word.back.models.database;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Words extends RealmObject {

    private RealmList<Word> words;

    public RealmList<Word> getWords() {
        return words;
    }

    public void setWords(RealmList<Word> words) {
        this.words = words;
    }
}