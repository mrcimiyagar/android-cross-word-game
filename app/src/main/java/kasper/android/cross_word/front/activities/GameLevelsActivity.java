package kasper.android.cross_word.front.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.database.GameLevels;
import kasper.android.cross_word.back.models.memory.GameLevel;

public class GameLevelsActivity extends AppCompatActivity {

    private TextView currentLevelTV;

    private TextView doneDataTV;
    private TextView pendingDataTV;

    private int doneCount;
    private int pendingCount;

    List<GameLevel> gameLevelList;

    int notDoneGameLevelId = -1;

    boolean nextLevel = false;
    boolean noMoreLevel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels);

        this.initViews();
        this.initDecoration();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int notDoneId = notDoneGameLevelId;
        this.initContent();
        if (nextLevel && !noMoreLevel) {
            if (notDoneId != notDoneGameLevelId && notDoneGameLevelId >= 0) {
                GameLevelsActivity.this.startActivity(new Intent(GameLevelsActivity.this
                        , GameSceneActivity.class).putExtra("game-level-id", notDoneGameLevelId));
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    private void initViews() {
        doneDataTV = findViewById(R.id.activity_offline_league_done_data_text_view);
        pendingDataTV = findViewById(R.id.activity_offline_league_pending_data_text_view);
        currentLevelTV = findViewById(R.id.activity_game_levels_current_level_text_view);
    }

    private void initDecoration() {

    }

    private void initContent() {
        gameLevelList = MyApp.getInstance().getDatabaseHelper().getGameLevels();
        doneCount = 0;
        pendingCount = 0;
        for (GameLevel mGameLevel : gameLevelList) {
            if (mGameLevel.isDone()) {
                doneCount++;
            } else {
                pendingCount++;
            }
        }
        doneDataTV.setText(doneCount + " ?????????? ?????????? ??????");
        pendingDataTV.setText(pendingCount + " ?????????? ???????? ??????????");

        int counter = 1;
        boolean foundNotDone = false;

        for (GameLevel gameLevel : gameLevelList) {

            if (!gameLevel.isDone()) {
                foundNotDone = true;
                notDoneGameLevelId = gameLevel.getId();
                break;
            }

            counter++;
        }

        if (foundNotDone) {

            noMoreLevel = false;

            currentLevelTV.setText("?????????? ?? " + counter);

            currentLevelTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextLevel = true;
                    GameLevelsActivity.this.startActivity(new Intent(GameLevelsActivity.this
                            , GameSceneActivity.class).putExtra("game-level-id", notDoneGameLevelId));
                }
            });
        }
        else {
            currentLevelTV.setText("?????? ?? ?????????? ?????????? ?????? ?????? .");
            noMoreLevel = true;
        }
    }
}