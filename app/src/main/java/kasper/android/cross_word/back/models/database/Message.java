package kasper.android.cross_word.back.models.database;

import io.realm.RealmObject;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public class Message extends RealmObject {

    private int id;
    private String content;
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
