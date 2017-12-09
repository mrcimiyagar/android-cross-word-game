package kasper.android.cross_word.back.models.database;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Messages extends RealmObject {

    private RealmList<Message> messages;

    public RealmList<Message> getMessages() {
        return messages;
    }

    public void setMessages(RealmList<Message> messages) {
        this.messages = messages;
    }
}