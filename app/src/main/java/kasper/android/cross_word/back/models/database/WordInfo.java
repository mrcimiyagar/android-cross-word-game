package kasper.android.cross_word.back.models.database;

import java.io.Serializable;

import io.realm.RealmObject;

public class WordInfo extends RealmObject implements Serializable {

    private String question;
    private String answer;
    private String answerIndex;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerIndex() {
        return answerIndex;
    }

    public void setAnswerIndex(String answerIndex) {
        this.answerIndex = answerIndex;
    }
}