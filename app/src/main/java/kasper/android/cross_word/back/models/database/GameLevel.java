package kasper.android.cross_word.back.models.database;

import java.io.Serializable;

import io.realm.RealmList;
import io.realm.RealmObject;

public class GameLevel extends RealmObject implements Serializable {
    private int id;
    private int number;
    private int prize;
    private int tableSize;
    private String tableData;
    private RealmList<WordInfo> words;
    private boolean hasQuestion;
    private boolean done;
    private int doneScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public int getTableSize() {
        return tableSize;
    }

    public void setTableSize(int tableSize) {
        this.tableSize = tableSize;
    }

    public String getTableData() {
        return tableData;
    }

    public void setTableData(String tableData) {
        this.tableData = tableData;
    }

    public RealmList<WordInfo> getWords() {
        return words;
    }

    public void setWords(RealmList<WordInfo> words) {
        this.words = words;
    }

    public boolean getHasQuestion() {
        return hasQuestion;
    }

    public void setHasQuestion(boolean hasQuestion) {
        this.hasQuestion = hasQuestion;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getDoneScore() {
        return doneScore;
    }

    public void setDoneScore(int doneScore) {
        this.doneScore = doneScore;
    }
}