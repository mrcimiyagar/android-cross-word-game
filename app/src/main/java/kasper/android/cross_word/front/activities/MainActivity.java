package kasper.android.cross_word.front.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import kasper.android.cross_word.R;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.Me;

public class MainActivity extends AppCompatActivity {

    BlurView topBV;
    BlurView pausePageBV;
    BlurView bottomBarBV;

    LinearLayout buttonsContainer;

    Handler buttonsAnimShowHandler;

    final int BLUR_SHOW_ANIMATION = 400;
    final int BLUR_HIDE_ANIMATION = 400;

    TextView moneyTV;
    TextView nameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initBlurViews();

        nameTV = findViewById(R.id.activity_main_name_text_view);
        moneyTV = findViewById(R.id.activity_main_money_text_view);

        buttonsContainer = findViewById(R.id.activity_main_buttons_container);

        for (int counter = 0; counter < buttonsContainer.getChildCount(); counter++) {
            buttonsContainer.getChildAt(counter).setScaleX(0.5f);
            buttonsContainer.getChildAt(counter).setScaleY(0.5f);
            buttonsContainer.getChildAt(counter).setAlpha(0);
        }

        buttonsAnimShowHandler = new Handler();

        buttonsAnimShowHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showBtn(0);
            }
        }, 250);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pausePageBV.animate().alpha(0).setDuration(BLUR_HIDE_ANIMATION).start();
        Me me = MyApp.getInstance().getDatabaseHelper().getMe();
        nameTV.setText(me.getName());
        moneyTV.setText("$ " + me.getScore());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_scale_out);
    }

    // buttons

    public void onMessagesBtnClicked(View view) {
        pausePageBV.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, MessagesActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onAccountBtnClicked(View view) {
        pausePageBV.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, SettingsActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onStoreBtnClicked(View view) {
        pausePageBV.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, StoreActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onOfflineGameBtnClicked(View view) {
        pausePageBV.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, GameLevelsActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onWordsBtnClicked(View view) {
        pausePageBV.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, WordsActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    public void onTourBtnClicked(View view) {
        pausePageBV.animate().alpha(1).setDuration(BLUR_SHOW_ANIMATION).start();
        startActivity(new Intent(this, TourActivity.class));
        overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
    }

    // ***

    private void initBlurViews() {
        float radius = 20;
        topBV = findViewById(R.id.activity_main_top_bar_blur_view);
        pausePageBV = findViewById(R.id.blurView);
        bottomBarBV = findViewById(R.id.activity_main_bottom_bar_blur_view);
        View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout (preferably)
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        Drawable windowBackground = decorView.getBackground();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            pausePageBV.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new RenderScriptBlur(this))
                    .blurRadius(radius);
            bottomBarBV.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new RenderScriptBlur(this))
                    .blurRadius(radius);
            topBV.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new RenderScriptBlur(this))
                    .blurRadius(radius);
        }
        else {
            pausePageBV.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new SupportRenderScriptBlur(this))
                    .blurRadius(radius);
            bottomBarBV.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new SupportRenderScriptBlur(this))
                    .blurRadius(radius);
            topBV.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new SupportRenderScriptBlur(this))
                    .blurRadius(radius);
        }
    }

    private void showBtn(final int index) {
        buttonsAnimShowHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (index < buttonsContainer.getChildCount()) {
                    buttonsContainer.getChildAt(index).animate().scaleX(1f).scaleY(1f).alpha(1).start();
                    showBtn(index + 1);
                }
            }
        }, 250);
    }
}
