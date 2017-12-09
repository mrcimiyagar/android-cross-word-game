package kasper.android.cross_word.back.callbacks;

import java.util.List;

import kasper.android.cross_word.back.models.memory.GameLevel;

public interface OnGameLevelsReadListener {
    void gameLevelsRead(List<GameLevel> gameLevels);
}
