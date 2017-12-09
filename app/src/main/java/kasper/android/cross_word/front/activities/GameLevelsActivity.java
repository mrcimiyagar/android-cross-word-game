package kasper.android.cross_word.front.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.GameLevel;
import kasper.android.cross_word.front.adapters.OffLegAdapter;
import kasper.android.cross_word.front.extras.LinearDecoration;

public class GameLevelsActivity extends AppCompatActivity {

    private RecyclerView scrollView;

    private TextView doneDataTV;
    private TextView pendingDataTV;

    private int doneCount;
    private int pendingCount;

    List<GameLevel> gameLevelList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_levels);

        this.initViews();
        this.initDecoration();
        this.initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.initContent();
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    private void initViews() {
        scrollView = findViewById(R.id.activity_game_levels_stack_view);
        doneDataTV = findViewById(R.id.activity_offline_league_done_data_text_view);
        pendingDataTV = findViewById(R.id.activity_offline_league_pending_data_text_view);
    }

    private void initListeners() {
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
    }

    private void initDecoration() {
        scrollView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        scrollView.addItemDecoration(new LinearDecoration((int)MyApp.getInstance().getDisplayHelper().dpToPx(8),
                (int)MyApp.getInstance().getDisplayHelper().dpToPx(8)));
    }

    private void initContent() {
        doneDataTV.setText(doneCount + " مرحله انجام شده");
        pendingDataTV.setText(pendingCount + " مرحله باقی مانده");
        scrollView.setAdapter(new OffLegAdapter(GameLevelsActivity.this, gameLevelList));
    }
}