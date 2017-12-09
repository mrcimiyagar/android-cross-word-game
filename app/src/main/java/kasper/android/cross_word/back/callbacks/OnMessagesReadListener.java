package kasper.android.cross_word.back.callbacks;

import java.util.List;

import kasper.android.cross_word.back.models.memory.Message;

/**
 * Created by keyhan1376 on 11/18/2017.
 */

public interface OnMessagesReadListener {
    void messagesRead(List<Message> messages);
}
