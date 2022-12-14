package kasper.android.cross_word.back.models.memory;

/**
 * Created by keyhan1376 on 12/1/2017.
 */

public class Me {
    private long playerId;
    private String playerKey;
    private String email;
    private String name;
    private int score;
    private int money;
    private Tournament lastTour;
    private Tournament currTour;
    private String accountNumber;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerKey() {
        return playerKey;
    }

    public void setPlayerKey(String playerKey) {
        this.playerKey = playerKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Tournament getLastTour() {
        return lastTour;
    }

    public void setLastTour(Tournament lastTour) {
        this.lastTour = lastTour;
    }

    public Tournament getCurrTour() {
        return this.currTour;
    }

    public void setCurrTour(Tournament currTour) {
        this.currTour = currTour;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
