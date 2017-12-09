package kasper.android.cross_word.back.callbacks;

import java.util.List;

import kasper.android.cross_word.back.models.memory.Word;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public interface OnWordsReadListener {
    void wordsRead(List<Word> words);
}
