package kasper.android.cross_word.back.helpers;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import kasper.android.cross_word.back.models.database.GameLevels;
import kasper.android.cross_word.back.models.database.Me;
import kasper.android.cross_word.back.models.database.Messages;
import kasper.android.cross_word.back.models.database.WordInfo;
import kasper.android.cross_word.back.models.database.Words;
import kasper.android.cross_word.back.models.memory.GameLevel;
import kasper.android.cross_word.back.models.memory.Message;
import kasper.android.cross_word.back.models.memory.Tournament;
import kasper.android.cross_word.back.models.memory.Word;

public class DatabaseHelper {

    public DatabaseHelper(Context context) {

        Realm.init(context);

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (realm.where(Me.class).count() == 0) {

            Me me = realm.createObject(Me.class);
            me.setPlayerId(-1);
            me.setPlayerKey("");
            me.setEmail("");
            me.setName("کاربر بی نام");
            me.setScore(0);

            kasper.android.cross_word.back.models.database.Tournament dLastTour =
                    realm.createObject(kasper.android.cross_word.back.models.database.Tournament.class);
            dLastTour.setId(-1);
            dLastTour.setPlayersCount(0);
            dLastTour.setLeftDays(0);
            dLastTour.setTotalDays(0);
            dLastTour.setActive(false);
            me.setLastTour(dLastTour);

            kasper.android.cross_word.back.models.database.Tournament dCurrTour =
                    realm.createObject(kasper.android.cross_word.back.models.database.Tournament.class);
            dCurrTour.setId(-1);
            dCurrTour.setPlayersCount(0);
            dCurrTour.setLeftDays(0);
            dCurrTour.setTotalDays(0);
            dCurrTour.setActive(false);
            me.setCurrTour(dCurrTour);
        }

        if (realm.where(GameLevels.class).count() == 0) {
            realm.createObject(GameLevels.class);
        }

        if (realm.where(Messages.class).count() == 0) {
            realm.createObject(Messages.class);
        }

        if (realm.where(Words.class).count() == 0) {
            realm.createObject(Words.class);
        }

        realm.commitTransaction();
        realm.close();
    }

    public kasper.android.cross_word.back.models.memory.Me getMe() {

        Realm realm = Realm.getDefaultInstance();

        Me dMe = realm.where(Me.class).findFirst();
        kasper.android.cross_word.back.models.memory.Me mMe =
                new kasper.android.cross_word.back.models.memory.Me();

        mMe.setPlayerId(dMe.getPlayerId());
        mMe.setPlayerKey(dMe.getPlayerKey());
        mMe.setEmail(dMe.getEmail());
        mMe.setName(dMe.getName());
        mMe.setScore(dMe.getScore());

        kasper.android.cross_word.back.models.database.Tournament dLastTour = dMe.getLastTour();
        Tournament mLastTour = new Tournament();
        mLastTour.setId(dLastTour.getId());
        mLastTour.setActive(dLastTour.isActive());
        mLastTour.setPlayersCount(dLastTour.getPlayersCount());
        mLastTour.setTotalDays(dLastTour.getTotalDays());
        mLastTour.setLeftDays(dLastTour.getLeftDays());
        mMe.setLastTour(mLastTour);

        kasper.android.cross_word.back.models.database.Tournament dCurrTour = dMe.getCurrTour();
        Tournament mCurrTour = new Tournament();
        mCurrTour.setId(dCurrTour.getId());
        mCurrTour.setActive(dCurrTour.isActive());
        mCurrTour.setPlayersCount(dCurrTour.getPlayersCount());
        mCurrTour.setTotalDays(dCurrTour.getTotalDays());
        mCurrTour.setLeftDays(dCurrTour.getLeftDays());
        mMe.setCurrTour(mCurrTour);

        realm.close();
        return mMe;
    }

    public void updateMe(kasper.android.cross_word.back.models.memory.Me me) {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Me dMe = realm.where(Me.class).findFirst();
        dMe.setPlayerId(me.getPlayerId());
        dMe.setPlayerKey(me.getPlayerKey());
        dMe.setEmail(me.getEmail());
        dMe.setName(me.getName());
        dMe.setScore(me.getScore());

        Tournament mLastTour = me.getLastTour();
        kasper.android.cross_word.back.models.database.Tournament dLastTour = dMe.getLastTour();
        dLastTour.setId(mLastTour.getId());
        dLastTour.setActive(mLastTour.isActive());
        dLastTour.setPlayersCount(mLastTour.getPlayersCount());
        dLastTour.setLeftDays(mLastTour.getLeftDays());
        dLastTour.setTotalDays(mLastTour.getTotalDays());
        dMe.setLastTour(dLastTour);

        Tournament mCurrTour = me.getCurrTour();
        kasper.android.cross_word.back.models.database.Tournament dCurrTour = dMe.getCurrTour();
        dCurrTour.setId(mCurrTour.getId());
        dCurrTour.setActive(mCurrTour.isActive());
        dCurrTour.setPlayersCount(mCurrTour.getPlayersCount());
        dCurrTour.setLeftDays(mCurrTour.getLeftDays());
        dCurrTour.setTotalDays(mCurrTour.getTotalDays());
        dMe.setCurrTour(dCurrTour);

        Log.d("KasperLogger", new Gson().toJson(me.getCurrTour()));

        realm.commitTransaction();
        realm.close();
    }

    public void addGameLevels(List<GameLevel> gameLevels) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        GameLevels gameLevelsContainer = realm.where(GameLevels.class).findFirst();
        for (GameLevel mGameLevel : gameLevels) {
            kasper.android.cross_word.back.models.database.GameLevel dGameLevel = realm
                    .createObject(kasper.android.cross_word.back.models.database.GameLevel.class);
            dGameLevel.setId(mGameLevel.getId());
            dGameLevel.setDone(dGameLevel.isDone());
            dGameLevel.setPrize(mGameLevel.getPrize());
            dGameLevel.setTableData(mGameLevel.getTableData());
            dGameLevel.setTableSize(mGameLevel.getTableSize());
            dGameLevel.setHasQuestion(mGameLevel.getHasQuestion());
            dGameLevel.setNumber(0);
            RealmList<WordInfo> wordInfoList = new RealmList<>();
            for (kasper.android.cross_word.back.models.memory.WordInfo mWordInfo : mGameLevel.getWords()) {
                WordInfo dWordInfo = realm.createObject(WordInfo.class);
                dWordInfo.setQuestion(mWordInfo.getQuestion());
                dWordInfo.setAnswer(mWordInfo.getAnswer());
                dWordInfo.setAnswerIndex(mWordInfo.getAnswerIndex());
                wordInfoList.add(dWordInfo);
            }
            dGameLevel.setWords(wordInfoList);
            gameLevelsContainer.getGameLevels().add(dGameLevel);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void mergeGameLevelsWith(List<GameLevel> gameLevels) {
        Realm realm = Realm.getDefaultInstance();
        GameLevels gameLevelsContainer = realm.where(GameLevels.class).findFirst();
        ArrayList<GameLevel> newGameLevels = new ArrayList<>();
        for (GameLevel gameLevel : gameLevels) {
            if (gameLevelsContainer.getGameLevels().where().equalTo("id", gameLevel.getId()).count() == 0) {
                newGameLevels.add(gameLevel);
            }
        }
        realm.close();
        if (newGameLevels.size() > 0) {
            this.addGameLevels(newGameLevels);
        }
    }

    public void addMessages(List<Message> messages) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Messages messagesContainer = realm.where(Messages.class).findFirst();
        for (Message mMessage : messages) {
            kasper.android.cross_word.back.models.database.Message dMessage = realm
                    .createObject(kasper.android.cross_word.back.models.database.Message.class);
            dMessage.setId(mMessage.getId());
            dMessage.setContent(mMessage.getContent());
            dMessage.setTime(mMessage.getTime());
            messagesContainer.getMessages().add(dMessage);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void mergeMessagesWith(List<Message> messages) {
        Log.d("KasperLoggGGER", "hello 2 " + messages.size());
        Realm realm = Realm.getDefaultInstance();
        Messages messagesContainer = realm.where(Messages.class).findFirst();
        ArrayList<Message> newMessages = new ArrayList<>();
        for (Message message : messages) {
            if (messagesContainer.getMessages().where().equalTo("id", message.getId()).count() == 0) {
                newMessages.add(message);
            }
        }
        realm.close();
        if (newMessages.size() > 0) {
            Log.d("KasperLoggGGER", "hello 3 " + newMessages.size());
            this.addMessages(newMessages);
        }
    }

    public void addWords(List<Word> words) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Words wordsContainer = realm.where(Words.class).findFirst();
        for (Word mWord : words) {
            kasper.android.cross_word.back.models.database.Word dWord = realm
                    .createObject(kasper.android.cross_word.back.models.database.Word.class);
            dWord.setId(mWord.getId());
            dWord.setWord(mWord.getWord());
            dWord.setMeaning(mWord.getMeaning());
            wordsContainer.getWords().add(dWord);
        }
        realm.commitTransaction();
        realm.close();
    }

    public void mergeWordsWith(List<Word> words) {
        Realm realm = Realm.getDefaultInstance();
        Words wordsContainer = realm.where(Words.class).findFirst();
        ArrayList<Word> newWords = new ArrayList<>();
        for (Word word : words) {
            if (wordsContainer.getWords().where().equalTo("id", word.getId()).count() == 0) {
                newWords.add(word);
            }
        }
        realm.close();
        if (newWords.size() > 0) {
            this.addWords(newWords);
        }
    }

    public boolean recheckGameLevels(List<Integer> availableIds) {
        return this.recheckGameLevels(new HashSet<>(availableIds));
    }

    public boolean recheckGameLevels(HashSet<Integer> availableIds) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        GameLevels gameLevelsContainer = realm.where(GameLevels.class).findFirst();
        HashSet<Integer> expiredGameLevels = new HashSet<>();
        for (kasper.android.cross_word.back.models.database.GameLevel dGameLevel : gameLevelsContainer.getGameLevels()) {
            if (!availableIds.contains(dGameLevel.getId())) {
                expiredGameLevels.add(dGameLevel.getId());
            }
        }
        for (Integer expiredId : expiredGameLevels) {
            gameLevelsContainer.getGameLevels().where().equalTo("id", expiredId).findFirst().deleteFromRealm();
        }
        realm.commitTransaction();
        boolean additionalData = false;
        for (int id : availableIds) {
            if (gameLevelsContainer.getGameLevels().where().equalTo("id", id).count() == 0) {
                additionalData = true;
                break;
            }
        }
        realm.close();
        return additionalData;
    }

    public boolean recheckMessages(List<Integer> availableIds) {
        return this.recheckMessages(new HashSet<>(availableIds));
    }

    public boolean recheckMessages(HashSet<Integer> availableIds) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Messages messagesContainer = realm.where(Messages.class).findFirst();
        HashSet<Integer> expiredMessages = new HashSet<>();
        for (kasper.android.cross_word.back.models.database.Message dMessage : messagesContainer.getMessages()) {
            if (!availableIds.contains(dMessage.getId())) {
                expiredMessages.add(dMessage.getId());
            }
        }
        for (Integer expiredId : expiredMessages) {
            messagesContainer.getMessages().where().equalTo("id", expiredId).findFirst().deleteFromRealm();
        }
        realm.commitTransaction();
        boolean additionalData = false;
        for (int id : availableIds) {
            if (messagesContainer.getMessages().where().equalTo("id", id).count() == 0) {
                additionalData = true;
                break;
            }
        }
        realm.close();
        return additionalData;
    }

    public boolean recheckWords(List<Integer> availableIds) {
        return this.recheckWords(new HashSet<>(availableIds));
    }

    public boolean recheckWords(HashSet<Integer> availableIds) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Words wordsContainer = realm.where(Words.class).findFirst();
        HashSet<Integer> expiredWords = new HashSet<>();
        for (kasper.android.cross_word.back.models.database.Word dWord : wordsContainer.getWords()) {
            if (!availableIds.contains(dWord.getId())) {
                expiredWords.add(dWord.getId());
            }
        }
        for (Integer expiredId : expiredWords) {
            wordsContainer.getWords().where().equalTo("id", expiredId).findFirst().deleteFromRealm();
        }
        realm.commitTransaction();
        boolean additionalData = false;
        for (int id : availableIds) {
            if (wordsContainer.getWords().where().equalTo("id", id).count() == 0) {
                additionalData = true;
                break;
            }
        }
        realm.close();
        return additionalData;
    }

    public List<GameLevel> getGameLevels() {
        List<GameLevel> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        GameLevels gameLevelContainer = realm.where(GameLevels.class).findFirst();
        for (kasper.android.cross_word.back.models.database.GameLevel dGameLevel : gameLevelContainer.getGameLevels()) {
            GameLevel mGameLevel = new GameLevel();
            mGameLevel.setId(dGameLevel.getId());
            mGameLevel.setPrize(dGameLevel.getPrize());
            mGameLevel.setTableData(dGameLevel.getTableData());
            mGameLevel.setTableSize(dGameLevel.getTableSize());
            mGameLevel.setNumber(0);
            mGameLevel.setDone(dGameLevel.isDone());
            mGameLevel.setDoneScore(dGameLevel.getDoneScore());
            mGameLevel.setHasQuestion(dGameLevel.getHasQuestion());
            ArrayList<kasper.android.cross_word.back.models.memory.WordInfo> wordInfos = new ArrayList<>();
            for (WordInfo dWordInfo : dGameLevel.getWords()) {
                kasper.android.cross_word.back.models.memory.WordInfo mWordInfo = new kasper.android.cross_word.back.models.memory.WordInfo();
                mWordInfo.setQuestion(dWordInfo.getQuestion());
                mWordInfo.setAnswer(dWordInfo.getAnswer());
                mWordInfo.setAnswerIndex(dWordInfo.getAnswerIndex());
                wordInfos.add(mWordInfo);
            }
            mGameLevel.setWords(wordInfos);
            result.add(mGameLevel);
        }
        realm.close();
        return result;
    }

    public List<Message> getMessages() {
        List<Message> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        Messages messagesContainer = realm.where(Messages.class).findFirst();
        for (kasper.android.cross_word.back.models.database.Message dMessage : messagesContainer.getMessages()) {
            Message mMessage = new Message();
            mMessage.setId(dMessage.getId());
            mMessage.setContent(dMessage.getContent());
            mMessage.setTime(dMessage.getTime());
            result.add(mMessage);
        }
        realm.close();
        return result;
    }

    public List<Word> getWords() {
        List<Word> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        Words wordsContainer = realm.where(Words.class).findFirst();
        for (kasper.android.cross_word.back.models.database.Word dWord : wordsContainer.getWords()) {
            Word mWord = new Word();
            mWord.setId(dWord.getId());
            mWord.setWord(dWord.getWord());
            mWord.setMeaning(dWord.getMeaning());
            result.add(mWord);
        }
        realm.close();
        return result;
    }

    public GameLevel getGameLevelById(int id) {
        GameLevel mGameLevel = null;
        Realm realm = Realm.getDefaultInstance();
        kasper.android.cross_word.back.models.database.GameLevel dGameLevel = realm.where(GameLevels
                .class).findFirst().getGameLevels().where().equalTo("id", id).findFirst();
        if (dGameLevel != null) {
            mGameLevel = new GameLevel();
            mGameLevel.setId(dGameLevel.getId());
            mGameLevel.setPrize(dGameLevel.getPrize());
            mGameLevel.setTableData(dGameLevel.getTableData());
            mGameLevel.setTableSize(dGameLevel.getTableSize());
            mGameLevel.setNumber(0);
            mGameLevel.setDone(dGameLevel.isDone());
            mGameLevel.setDoneScore(dGameLevel.getDoneScore());
            mGameLevel.setHasQuestion(dGameLevel.getHasQuestion());
            ArrayList<kasper.android.cross_word.back.models.memory.WordInfo> wordInfos = new ArrayList<>();
            for (WordInfo dWordInfo : dGameLevel.getWords()) {
                kasper.android.cross_word.back.models.memory.WordInfo mWordInfo = new kasper.android.cross_word.back.models.memory.WordInfo();
                mWordInfo.setQuestion(dWordInfo.getQuestion());
                mWordInfo.setAnswer(dWordInfo.getAnswer());
                mWordInfo.setAnswerIndex(dWordInfo.getAnswerIndex());
                wordInfos.add(mWordInfo);
            }
            mGameLevel.setWords(wordInfos);
        }
        realm.close();
        return mGameLevel;
    }

    public void notifyPlayerFinishedGameLevel(int id) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        kasper.android.cross_word.back.models.database.GameLevel dGameLevel =
        realm.where(GameLevels.class).findFirst().getGameLevels()
                .where().equalTo("id", id).findFirst();
        if (!dGameLevel.isDone()) {
            dGameLevel.setDone(true);
            Me me = realm.where(Me.class).findFirst();
            me.setScore(me.getScore() + dGameLevel.getPrize());
        }
        realm.commitTransaction();
        realm.close();
    }
}