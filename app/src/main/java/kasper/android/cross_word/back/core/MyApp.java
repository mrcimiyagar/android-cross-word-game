package kasper.android.cross_word.back.core;

import android.app.Application;

import java.util.List;

import kasper.android.cross_word.back.callbacks.OnGameLevelsCheckedListener;
import kasper.android.cross_word.back.callbacks.OnGameLevelsReadListener;
import kasper.android.cross_word.back.callbacks.OnMessagesCheckedListener;
import kasper.android.cross_word.back.callbacks.OnMessagesReadListener;
import kasper.android.cross_word.back.callbacks.OnMyScoreUpdatedListener;
import kasper.android.cross_word.back.callbacks.OnTourDataReadListener;
import kasper.android.cross_word.back.callbacks.OnWordsCheckedListener;
import kasper.android.cross_word.back.callbacks.OnWordsReadListener;
import kasper.android.cross_word.back.callbacks.OnAppLoadCompleteListener;
import kasper.android.cross_word.back.helpers.DatabaseHelper;
import kasper.android.cross_word.back.helpers.DisplayHelper;
import kasper.android.cross_word.back.helpers.NetworkHelper;
import kasper.android.cross_word.back.models.memory.GameLevel;
import kasper.android.cross_word.back.models.memory.Me;
import kasper.android.cross_word.back.models.memory.Message;
import kasper.android.cross_word.back.models.memory.Tournament;
import kasper.android.cross_word.back.models.memory.Word;

public class MyApp extends Application {

    private static MyApp instance;
    public static MyApp getInstance() {
        return instance;
    }

    private DatabaseHelper databaseHelper;
    public DatabaseHelper getDatabaseHelper() {
        return this.databaseHelper;
    }

    private NetworkHelper networkHelper;
    public NetworkHelper getNetworkHelper() {
        return this.networkHelper;
    }

    private DisplayHelper displayHelper;
    public DisplayHelper getDisplayHelper() {
        return this.displayHelper;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public void startMachine(OnAppLoadCompleteListener callback) {
        this.displayHelper = new DisplayHelper();
        this.networkHelper = new NetworkHelper();
        this.databaseHelper = new DatabaseHelper(this);
        this.checkUpdatesOnGameContent(callback);
    }

    private final boolean[] syncTaskState = new boolean[5];

    private void checkUpdatesOnGameContent(final OnAppLoadCompleteListener callback) {
        for (int counter = 0; counter < this.syncTaskState.length; counter++) {
            this.syncTaskState[counter] = false;
        }
        if (this.networkHelper.isNetworkAvailable()) {
            this.networkHelper.recheckGameLevelsFromServer(new OnGameLevelsCheckedListener() {
                @Override
                public void gameLevelsChecked(List<Integer> gameLevelIds) {
                    if (MyApp.this.databaseHelper.recheckGameLevels(gameLevelIds)) {
                        MyApp.this.networkHelper.readGameLevelsFromServer(new OnGameLevelsReadListener() {
                            @Override
                            public void gameLevelsRead(List<GameLevel> gameLevels) {
                                MyApp.this.databaseHelper.mergeGameLevelsWith(gameLevels);
                                MyApp.this.signMiniSyncTaskAsDone(0, callback);
                            }
                        });
                    }
                    else {
                        MyApp.this.signMiniSyncTaskAsDone(0, callback);
                    }
                }
            });
            this.networkHelper.recheckMessagesFromServer(new OnMessagesCheckedListener() {
                @Override
                public void messagesChecked(List<Integer> messageIds) {
                    if (MyApp.this.databaseHelper.recheckMessages(messageIds)) {
                        MyApp.this.networkHelper.readMessagesFromServer(new OnMessagesReadListener() {
                            @Override
                            public void messagesRead(List<Message> messages) {
                                MyApp.this.databaseHelper.mergeMessagesWith(messages);
                                MyApp.this.signMiniSyncTaskAsDone(1, callback);
                            }
                        });
                    }
                    else {
                        MyApp.this.signMiniSyncTaskAsDone(1, callback);
                    }
                }
            });
            this.networkHelper.recheckWordsFromServer(new OnWordsCheckedListener() {
                @Override
                public void wordsChecked(List<Integer> wordIds) {
                    if (MyApp.this.databaseHelper.recheckWords(wordIds)) {
                        MyApp.this.networkHelper.readWordsFromServer(new OnWordsReadListener() {
                            @Override
                            public void wordsRead(List<Word> words) {
                                MyApp.this.databaseHelper.mergeWordsWith(words);
                                MyApp.this.signMiniSyncTaskAsDone(2, callback);
                            }
                        });
                    }
                    else {
                        MyApp.this.signMiniSyncTaskAsDone(2, callback);
                    }
                }
            });
            this.networkHelper.readTourDataFromServer(new OnTourDataReadListener() {
                @Override
                public void tourDataRead(Tournament tournament) {
                    Me me = MyApp.this.databaseHelper.getMe();
                    me.setCurrTour(tournament);
                    MyApp.getInstance().getDatabaseHelper().updateMe(me);
                    MyApp.this.signMiniSyncTaskAsDone(3, callback);
                }
            });

            Me me = this.databaseHelper.getMe();

            if (me.getPlayerId() >= 0 && me.getPlayerKey().length() > 0 && me.getName().length() > 0 && me.getScore() > 0) {

                this.networkHelper.updateMyScoreInServer(me.getPlayerId(), me.getPlayerKey(), me.getName(), me.getScore()
                        , new OnMyScoreUpdatedListener() {
                    @Override
                    public void myScoreUpdated() {
                        MyApp.this.signMiniSyncTaskAsDone(4, callback);
                    }
                });
            }
            else {

                MyApp.this.signMiniSyncTaskAsDone(4, callback);
            }
        }
        else {
            callback.onlineSyncTaskCompleted();
        }
    }

    private void signMiniSyncTaskAsDone(int index, OnAppLoadCompleteListener callback) {
        synchronized (this.syncTaskState) {
            this.syncTaskState[index] = true;
            checkSyncTaskCompletion(callback);
        }
    }

    private void checkSyncTaskCompletion(OnAppLoadCompleteListener callback) {
        boolean syncCompleted = true;
        for (boolean b : this.syncTaskState) {
            if (!b) {
                syncCompleted = false;
                break;
            }
        }
        if (syncCompleted) {
            callback.onlineSyncTaskCompleted();
        }
    }
}