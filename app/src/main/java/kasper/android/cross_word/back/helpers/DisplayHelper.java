package kasper.android.cross_word.back.helpers;

import kasper.android.cross_word.back.core.MyApp;

public class DisplayHelper {
    public float dpToPx(float dp) {
        return MyApp.getInstance().getResources().getDisplayMetrics().density * dp;
    }
}
