package kasper.android.cross_word.back.models.memory;

import java.io.Serializable;

public class WordInfo implements Serializable {

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