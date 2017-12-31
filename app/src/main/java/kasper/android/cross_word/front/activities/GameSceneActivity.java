package kasper.android.cross_word.front.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur;
import com.jcminarro.roundkornerlayout.RoundKornerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import kasper.android.cross_word.R;
import kasper.android.cross_word.back.callbacks.OnMyScoreUpdatedListener;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.GameLevel;
import kasper.android.cross_word.back.models.memory.Me;
import kasper.android.cross_word.back.models.memory.WordInfo;
import kasper.android.cross_word.front.adapters.FilledTableAdapter;

public class GameSceneActivity extends AppCompatActivity {

    final int BLUR_SHOW_ANIMATION = 300;
    final int BLUR_HIDE_ANIMATION = 300;

    BlurView blurView;
    RoundKornerFrameLayout selectedWordContainer;
    TextView selectedWordTV;
    RoundKornerFrameLayout gameBoardContainer;
    RecyclerView tableRV;

    FilledTableAdapter tableAdapter;
    GameLevel gameLevel;
    boolean[] nodeUsed;
    int[][] answerIndices;
    boolean[] wordsFound;
    int tableSize;
    List<Integer> wordPosesX;
    List<Integer> wordPosesY;
    List<Integer> wordIndices;
    String selectedWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_scene);

        this.collectArguments();
        this.initBlurViews();
        this.initGameLogicController();
        this.initViews();
        this.initDecoration();
        this.initBoardData();
        this.initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        blurView.animate().alpha(0).setDuration(BLUR_HIDE_ANIMATION).start();
        boolean gameFinished = true;
        for (boolean b : wordsFound) {
            if (!b) {
                gameFinished = false;
                break;
            }
        }
        if (gameFinished) {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        blurView.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivityForResult(new Intent(this, GameExitActivity.class), 1);
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String resultData = data.getExtras().getString("choice");
                if (resultData != null && resultData.equals("yes")) {
                    this.finish();
                }
            }
        }
        else if (requestCode == 456) {
            if (resultCode == RESULT_OK) {
                String resultData = data.getExtras().getString("choice");
                if (resultData != null && resultData.equals("yes")) {
                    Me me = MyApp.getInstance().getDatabaseHelper().getMe();
                    if (me.getScore() + me.getMoney() > 50) {
                        if (me.getScore() >= 50) {
                            me.setScore(me.getScore() - 50);
                        }
                        else {
                            int price = 50;
                            price -= me.getScore();
                            me.setScore(0);
                            me.setMoney(me.getMoney() - price);
                        }
                        MyApp.getInstance().getDatabaseHelper().updateMe(me);
                        for (int counter = 0; counter < gameLevel.getWords().size(); counter++) {
                            if (!wordsFound[counter]) {
                                Pair<Boolean, Integer> wordState = new Pair<>(true, counter);
                                doWordFindingFinishJob(wordState);
                            }
                        }
                    }
                    else {
                        Toast.makeText(this, "امتیاز کافی ندارید.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onHelpBtnClicked(View view) {
        blurView.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivityForResult(new Intent(this, UseHelpActivity.class), 456);
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onStoreBtnClicked(View view) {
        blurView.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, StoreActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onGameQuestsBtnClicked(View view) {
        blurView.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, GameQuestionsActivity.class)
                .putExtra("word-infos", gameLevel.getWords())
                .putExtra("words-found", wordsFound));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    private void collectArguments() {
        int gameLevelId = getIntent().getExtras().getInt("game-level-id");
        this.gameLevel = MyApp.getInstance().getDatabaseHelper().getGameLevelById(gameLevelId);
        this.nodeUsed = new boolean[this.gameLevel.getTableData().length()];
        for (int counter = 0; counter < this.gameLevel.getTableData().length(); counter++) {
            this.nodeUsed[counter] = false;
        }
        this.answerIndices = new int[this.gameLevel.getWords().size()][];
        for (int counter = 0; counter < this.gameLevel.getWords().size(); counter++) {
            String[] answerIndicesStrArr = this.gameLevel.getWords().get(counter).getAnswerIndex().split("-");
            this.answerIndices[counter] = new int[answerIndicesStrArr.length];
            for (int nCounter = 0; nCounter < answerIndicesStrArr.length; nCounter++) {
                this.answerIndices[counter][nCounter] = Integer.parseInt(answerIndicesStrArr[nCounter]);
            }
        }
    }

    private void initGameLogicController() {
        this.wordsFound = new boolean[this.gameLevel.getWords().size()];
        for (int counter = 0; counter < this.wordsFound.length; counter++) {
            this.wordsFound[counter] = false;
        }
        this.wordPosesX = new ArrayList<>();
        this.wordPosesY = new ArrayList<>();
        this.wordIndices = new ArrayList<>();
        this.tableSize = gameLevel.getTableSize();
        this.selectedWord = "";
    }

    private void initViews() {
        gameBoardContainer = findViewById(R.id.activity_game_scene_game_board_container);
        tableRV = findViewById(R.id.activity_game_scene_game_board);
        selectedWordContainer = findViewById(R.id.activity_game_scene_selected_word_container);
        selectedWordTV = findViewById(R.id.activity_game_scene_selected_word_text_view);
    }

    private void initDecoration() {
        int screenSize = getResources().getDisplayMetrics().widthPixels;
        int correctWidth = screenSize - (int)(2 * 16 * getResources().getDisplayMetrics().density);
        RelativeLayout.LayoutParams boardLP = new RelativeLayout.LayoutParams(correctWidth, correctWidth);
        boardLP.setMargins(0, (int) MyApp.getInstance().getDisplayHelper().dpToPx(102), 0, 0);
        boardLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
        gameBoardContainer.setLayoutParams(boardLP);
        CardView.LayoutParams tableLP = new CardView.LayoutParams(correctWidth, correctWidth);
        tableRV.setLayoutParams(tableLP);
        tableRV.setLayoutManager(new GridLayoutManager(this, gameLevel.getTableSize()));
    }

    private void initBoardData() {
        String[] tableDataArr = new String[gameLevel.getTableData().length()];
        for (int counter = 0; counter < tableDataArr.length; counter++) {
            tableDataArr[counter] = gameLevel.getTableData().charAt(counter) + "";
        }
        tableAdapter = new FilledTableAdapter(tableDataArr);
        tableRV.setAdapter(tableAdapter);
    }

    private void initListeners() {
        tableRV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {

                    final int blockSize = (tableRV.getMeasuredWidth() / tableSize);

                    int blockX = (int)(event.getX() / blockSize);
                    int blockY = (int)(event.getY() / blockSize);

                    if (Math.abs(event.getX() - (blockX + 0.5f) * blockSize) < blockSize * 0.3f
                            && Math.abs(event.getY() - (blockY + 0.5f) * blockSize) < blockSize * 0.3f) {

                        int wordIndex = blockY * tableSize + blockX;

                        if (wordIndices.contains(wordIndex)) {
                            return true;
                        }

                        if (wordIndices.size() == 0) {

                            doWordFindingContinueJob(blockX, blockY, wordIndex);
                        }
                        else {

                            int lastBX = wordPosesX.get(wordPosesX.size() - 1);
                            int lastBY = wordPosesY.get(wordPosesY.size() - 1);

                            if (Math.abs(lastBX - blockX) <= 1 && Math.abs(lastBY - blockY) <= 1) {

                                Log.d("KasperLogger", "hello ! " + blockX + " " + blockY);

                                doWordFindingContinueJob(blockX, blockY, wordIndex);
                            }
                        }
                    }

                    return true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    doWordFindingFinishJob(null);
                }

                return false;
            }
        });
    }

    private void doWordFindingContinueJob(int blockX, int blockY, int charIndex) {
        if (charIndex < gameLevel.getTableData().length() && !this.nodeUsed[charIndex]) {
            wordPosesX.add(blockX);
            wordPosesY.add(blockY);
            wordIndices.add(charIndex);
            selectedWord += gameLevel.getTableData().charAt(charIndex);
            selectedWordTV.setText(selectedWord);
            ((FilledTableAdapter) tableRV.getAdapter()).notifyItemSelected(charIndex);
        }
    }

    private void doWordFindingFinishJob(Pair<Boolean, Integer> wordState) {
        if (wordState == null) {
            wordState = checkWordInAnswers(wordIndices);
        }

        if (wordState.first) {
            ((FilledTableAdapter) tableRV.getAdapter()).notifyWordFound(wordIndices);
            wordsFound[wordState.second] = true;
            for (int nodeIndex : wordIndices) {
                nodeUsed[nodeIndex] = true;
            }
            if (checkAllWordsFoundState()) {
                MyApp.getInstance().getDatabaseHelper()
                        .notifyPlayerFinishedGameLevel(gameLevel.getId());
                Me me = MyApp.getInstance().getDatabaseHelper().getMe();
                MyApp.getInstance().getNetworkHelper().updateMyScoreInServer(me.getPlayerId(), me.getPlayerKey()
                        , me.getName(), me.getScore(), new OnMyScoreUpdatedListener() {
                            @Override
                            public void myScoreUpdated() {

                            }
                        });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        blurView.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
                        Intent intent = new Intent(GameSceneActivity.this, PresentActivity.class);
                        intent.putExtra("present-title", "پایان بازی");
                        intent.putExtra("present-content", "شما بردید !");
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
                    }
                }, 300);
            }
        } else {
            ((FilledTableAdapter) tableRV.getAdapter()).notifySelectionReset();
        }
        wordPosesX.clear();
        wordPosesY.clear();
        wordIndices.clear();
        selectedWord = "";
        selectedWordTV.setText(selectedWord);
    }

    private boolean checkAllWordsFoundState() {
        for (boolean wf : wordsFound) {
            if (!wf) {
                return false;
            }
        }
        return true;
    }

    private Pair<Boolean, Integer> checkWordInAnswers(List<Integer> wordIndices) {

        boolean match = true;
        int answerNumber = 0;
        for (int[] wordAnswerIndices : answerIndices) {
            match = true;
            if (wordAnswerIndices.length == wordIndices.size()) {
                for (int counter = 0; counter < wordAnswerIndices.length; counter++) {
                    if (wordIndices.get(counter) != wordAnswerIndices[counter]) {
                        match = false;
                        break;
                    }
                }
            }
            else {
                match = false;
            }
            if (match) {
                break;
            }
            answerNumber++;
        }

        return new Pair<>(match, answerNumber);
    }

    private void initBlurViews() {
        float radius = 20;
        blurView = findViewById(R.id.activity_game_scene_blur_view);
        View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout (preferably)
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        Drawable windowBackground = decorView.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurView.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new RenderScriptBlur(this))
                    .blurRadius(radius);
        }
        else {
            blurView.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new SupportRenderScriptBlur(this))
                    .blurRadius(radius);
        }
    }
}
