package kasper.android.cross_word.back.models.database;

import io.realm.RealmList;
import io.realm.RealmObject;

public class GameLevels extends RealmObject {

    private RealmList<GameLevel> gameLevels;

    public RealmList<GameLevel> getGameLevels() {
        return gameLevels;
    }

    public void setGameLevels(RealmList<GameLevel> gameLevels) {
        this.gameLevels = gameLevels;
    }
}