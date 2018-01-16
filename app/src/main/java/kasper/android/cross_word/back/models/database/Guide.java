package kasper.android.cross_word.back.models.database;

import io.realm.RealmObject;

/**
 * Created by keyhan1376 on 1/16/2018.
 */

public class Guide extends RealmObject {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
