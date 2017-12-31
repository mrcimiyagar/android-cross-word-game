package kasper.android.cross_word.back.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import kasper.android.cross_word.back.callbacks.OnGameLevelsCheckedListener;
import kasper.android.cross_word.back.callbacks.OnGameLevelsReadListener;
import kasper.android.cross_word.back.callbacks.OnMessagesCheckedListener;
import kasper.android.cross_word.back.callbacks.OnMessagesReadListener;
import kasper.android.cross_word.back.callbacks.OnMyScoreUpdatedListener;
import kasper.android.cross_word.back.callbacks.OnMyTourDataReadListener;
import kasper.android.cross_word.back.callbacks.OnTourDataReadListener;
import kasper.android.cross_word.back.callbacks.OnTourPlayerAddedListener;
import kasper.android.cross_word.back.callbacks.OnTourPlayersReadListener;
import kasper.android.cross_word.back.callbacks.OnWordsCheckedListener;
import kasper.android.cross_word.back.callbacks.OnWordsReadListener;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.GameLevel;
import kasper.android.cross_word.back.models.memory.Message;
import kasper.android.cross_word.back.models.memory.TourPlayer;
import kasper.android.cross_word.back.models.memory.Tournament;
import kasper.android.cross_word.back.models.memory.Word;
import kasper.android.cross_word.back.models.memory.WordInfo;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkHelper {
    
    private final String LOG_TAG = "NetworkHelper";

    private final String serverAddress = "http://136.243.229.153";
    private final String playerFirstKey = "ds56f4gcvx1nr8y7j98wr765q4th314aryk657a65rt4h1b32xa1b658a5et74h";
    private final String playerSecondKey = "32g1j,687tu4k789q7ryj64aeb321a6e8r7g651b32adn1468at7eh65a1b35ad54fb";
    private final String gameLevelsControllerName = "GameLevels";
    private final String methodReadGameLevels = "ReadGameLevels";
    private final String methodReadGameLevelIds = "ReadGameLevelIds";
    private final String messagesControllerName = "Messages";
    private final String methodReadMessages = "ReadMessages";
    private final String methodReadMessageIds = "ReadMessageIds";
    private final String wordsControllerName = "Words";
    private final String methodReadWords = "ReadWords";
    private final String methodReadWordIds = "ReadWordIds";
    private final String tourPlayersControllerName = "TourPlayers";
    private final String methodReadTourData = "ReadTourData";
    private final String methodAddTourPlayer = "AddTourPlayer";
    private final String methodReadTopTourPlayers = "ReadTopTourPlayers";
    private final String methodReadMyTourData = "ReadMyTourData";
    private final String methodEditMyScore = "EditTourPlayer";

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApp.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void recheckGameLevelsFromServer(final OnGameLevelsCheckedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + gameLevelsControllerName
                            + "/" + methodReadGameLevelIds + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<Integer> gameLevelIds = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        int id = jsonArray.optInt(counter);
                        gameLevelIds.add(id);
                    }

                    callback.gameLevelsChecked(gameLevelIds);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.gameLevelsChecked(null);
                }
            }
        }).start();
    }

    public void readGameLevelsFromServer(final OnGameLevelsReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + gameLevelsControllerName
                            + "/" + methodReadGameLevels + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<GameLevel> gameLevels = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        GameLevel gameLevel = new GameLevel();
                        gameLevel.setId(jsonObject.getInt("id"));
                        gameLevel.setNumber(counter + 1);
                        gameLevel.setPrize(jsonObject.getInt("prize"));

                        String tableData = jsonObject.getString("tableData");

                        gameLevel.setTableData(tableData);

                        gameLevel.setTableSize((int)(Math.sqrt(gameLevel.getTableData().length())));

                        String questionData = jsonObject.getString("questionData");
                        String answerData = jsonObject.getString("answerData");

                        gameLevel.setHasQuestion(!questionData.equals("empty"));

                        String[] answerArray = answerData.split(",");
                        String[] questionArray = null;

                        if (gameLevel.getHasQuestion()) {
                            questionArray = questionData.split(",");
                        }
                        else {
                            questionArray = new String[answerArray.length];
                            for (int counter1 = 0; counter1 < questionArray.length; counter1++) {
                                questionArray[counter1] = "";
                            }
                        }

                        ArrayList<WordInfo> wordsList = new ArrayList<>();

                        for (int counter1 = 0; counter1 < answerArray.length; counter1++) {
                            WordInfo wordInfo = new WordInfo();
                            wordInfo.setQuestion(gameLevel.getHasQuestion() ? questionArray[counter1] : "");

                            String sAnswerData = answerArray[counter1];

                            wordInfo.setAnswerIndex(sAnswerData);

                            String answer = "";

                            String[] sAnswerDataPart = sAnswerData.split("-");

                            for (String aSAnswerDataPart : sAnswerDataPart) {
                                answer += (gameLevel.getTableData().charAt(Integer.parseInt(aSAnswerDataPart)) + "");
                            }

                            wordInfo.setAnswer(answer);

                            wordsList.add(wordInfo);
                        }

                        gameLevel.setWords(wordsList);

                        gameLevels.add(gameLevel);
                    }

                    callback.gameLevelsRead(gameLevels);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.gameLevelsRead(null);
                }
            }
        }).start();
    }

    public void recheckMessagesFromServer(final OnMessagesCheckedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + messagesControllerName
                            + "/" + methodReadMessageIds + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<Integer> messageIds = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        int id = jsonArray.optInt(counter);
                        messageIds.add(id);
                    }

                    callback.messagesChecked(messageIds);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.messagesChecked(null);
                }
            }
        }).start();
    }

    public void readMessagesFromServer(final OnMessagesReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + messagesControllerName
                            + "/" + methodReadMessages + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<Message> messages = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        Message message = new Message();
                        message.setId(jsonObject.getInt("id"));
                        message.setContent(jsonObject.getString("content"));
                        message.setTime(jsonObject.getLong("time"));

                        messages.add(message);
                    }

                    callback.messagesRead(messages);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.messagesRead(null);
                }
            }
        }).start();
    }

    public void recheckWordsFromServer(final OnWordsCheckedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            String urlStr = serverAddress + "/CrossWordGame/api/" + wordsControllerName
                                    + "/" + methodReadWordIds + "?firstKey=" + playerFirstKey + "&secondKey="
                                    + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                            Log.d(LOG_TAG, urlStr);

                            OkHttpClient client = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url(urlStr)
                                    .addHeader("Cache-Control", "no-cache")
                                    .build();
                            request.cacheControl().noCache();
                            Response response = client.newCall(request).execute();
                            String result = response.body().string();

                            Log.d(LOG_TAG, result);

                            JSONArray jsonArray = new JSONArray(result);

                            ArrayList<Integer> wordIds = new ArrayList<>();

                            for (int counter = 0; counter < jsonArray.length(); counter++) {
                                int id = jsonArray.optInt(counter);
                                wordIds.add(id);
                            }

                            callback.wordsChecked(wordIds);

                        } catch (Exception ignored) {
                            ignored.printStackTrace();
                            callback.wordsChecked(null);
                        }
                    }
                }).start();
            }
        }).start();
    }

    public void readWordsFromServer(final OnWordsReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + wordsControllerName
                            + "/" + methodReadWords + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);
                    Log.d(LOG_TAG, System.currentTimeMillis() + "");

                    JSONArray jsonArray = new JSONArray(result);

                    ArrayList<Word> words = new ArrayList<>();

                    for (int counter = 0; counter < jsonArray.length(); counter++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(counter);
                        Word word = new Word();
                        word.setId(jsonObject.getInt("id"));
                        word.setWord(jsonObject.getString("word"));
                        word.setMeaning(jsonObject.getString("meaning"));

                        words.add(word);
                    }

                    callback.wordsRead(words);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.wordsRead(null);
                }
            }
        }).start();
    }

    public void readTourDataFromServer(final OnTourDataReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + tourPlayersControllerName
                            + "/" + methodReadTourData + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONObject tourJO = new JSONObject(result);

                    long id = tourJO.getLong("id");
                    boolean active = tourJO.getBoolean("active");
                    int leftDays = tourJO.getInt("leftDays");
                    int totalDays = tourJO.getInt("totalDays");
                    long startMillis = tourJO.getLong("startMillis");
                    int playersCount = tourJO.getInt("playersCount");

                    Tournament tournament = new Tournament();
                    tournament.setId(id);
                    tournament.setActive(active);
                    tournament.setLeftDays(leftDays);
                    tournament.setTotalDays(totalDays);
                    tournament.setStartMillis(startMillis);
                    tournament.setPlayersCount(playersCount);

                    callback.tourDataRead(tournament);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.tourDataRead(null);
                }
            }
        }).start();
    }

    public void addTourPlayerToServer(final String name, final OnTourPlayerAddedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + tourPlayersControllerName
                            + "/" + methodAddTourPlayer + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&name=" + name;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    String[] resultParts = result.substring(1, result.length() - 1).split(",");

                    long playerId = Long.parseLong(resultParts[1]);
                    String passkey = resultParts[2];

                    callback.tourPlayerAdded(playerId, passkey);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void readTourPlayersFromServer(final OnTourPlayersReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + tourPlayersControllerName
                            + "/" + methodReadTopTourPlayers + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&updateVersion=" + System.currentTimeMillis();

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONArray playersJA = new JSONArray(result);

                    List<TourPlayer> players = new ArrayList<>();

                    for (int counter = 0; counter < playersJA.length(); counter++) {
                        JSONObject playerJO = (JSONObject) playersJA.get(counter);

                        long id = playerJO.getLong("id");
                        String name = playerJO.getString("name");
                        int score = playerJO.getInt("score");

                        TourPlayer tourPlayer = new TourPlayer();
                        tourPlayer.setId(id);
                        tourPlayer.setName(name);
                        tourPlayer.setScore(score);

                        players.add(tourPlayer);
                    }

                    callback.tourPlayersRead(players);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void readMyTourDataFromServer(final long id, final OnMyTourDataReadListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + tourPlayersControllerName
                            + "/" + methodReadMyTourData + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&id=" + id;

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    JSONObject playerJO = new JSONObject(result);

                    long id = playerJO.getLong("id");
                    String name = playerJO.getString("name");
                    int score = playerJO.getInt("score");
                    int rank = playerJO.getInt("rank");

                    TourPlayer tourPlayer = new TourPlayer();
                    tourPlayer.setId(id);
                    tourPlayer.setName(name);
                    tourPlayer.setScore(score);
                    tourPlayer.setRank(rank);

                    callback.myTourDataRead(tourPlayer);

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
        }).start();
    }

    public void updateMyScoreInServer(final long id, final String passkey, final String name, final int score
            , final OnMyScoreUpdatedListener callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    String urlStr = serverAddress + "/CrossWordGame/api/" + tourPlayersControllerName
                            + "/" + methodEditMyScore + "?firstKey=" + playerFirstKey + "&secondKey="
                            + playerSecondKey + "&id=" + id + "&passkey=" + passkey + "&name=" + name
                            + "&score=" + score;

                    Log.d("KasperLogger", passkey);

                    Log.d(LOG_TAG, urlStr);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(urlStr)
                            .addHeader("Cache-Control", "no-cache")
                            .build();
                    request.cacheControl().noCache();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();

                    Log.d(LOG_TAG, result);

                    callback.myScoreUpdated();

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                    callback.myScoreUpdated();
                }
            }
        }).start();
    }
}