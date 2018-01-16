package kasper.android.cross_word.back.models.database;

import io.realm.RealmObject;

/**
 * Created by keyhan1376 on 1/16/2018.
 */

public class Coins extends RealmObject {
    private int storeCoins;
    private int helpCoins;

    public int getStoreCoins() {
        return storeCoins;
    }

    public void setStoreCoins(int storeCoins) {
        this.storeCoins = storeCoins;
    }

    public int getHelpCoins() {
        return helpCoins;
    }

    public void setHelpCoins(int helpCoins) {
        this.helpCoins = helpCoins;
    }
}
