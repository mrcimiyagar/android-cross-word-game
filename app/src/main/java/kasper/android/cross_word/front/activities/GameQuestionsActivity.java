package kasper.android.cross_word.front.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.models.memory.WordInfo;
import kasper.android.cross_word.front.adapters.GameQuestsAdapter;

public class GameQuestionsActivity extends AppCompatActivity {

    private ArrayList<WordInfo> wordInfos;
    private boolean[] wordsFound;

    private RecyclerView questsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_questions);

        this.wordInfos = (ArrayList<WordInfo>) getIntent().getExtras().getSerializable("word-infos");
        this.wordsFound = getIntent().getExtras().getBooleanArray("words-found");

        questsRV = findViewById(R.id.activity_game_questions_recycler_view);
        questsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        questsRV.setAdapter(new GameQuestsAdapter(wordInfos, wordsFound));
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }
}
