package kasper.android.cross_word.front.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import kasper.android.cross_word.R;
import kasper.android.cross_word.back.callbacks.OnAppLoadCompleteListener;
import kasper.android.cross_word.back.core.MyApp;

public class StartupActivity extends AppCompatActivity {

    RelativeLayout logoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        this.logoContainer = findViewById(R.id.activity_startup_logo_container);

        loadBlurView();

        final long syncStartTime = System.currentTimeMillis();

        MyApp.getInstance().startMachine(new OnAppLoadCompleteListener() {
            @Override
            public void onlineSyncTaskCompleted() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long currentTime = System.currentTimeMillis();
                        long timeSpent = currentTime - syncStartTime;
                        if (timeSpent < 1500) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    gotoMainActivity();
                                }
                            }, 1500 - timeSpent);
                        }
                        else {
                            gotoMainActivity();
                        }
                    }
                });
            }
        });
    }

    private void loadBlurView() {
        float radius = 20;

        BlurView blurView = (BlurView) findViewById(R.id.blurView);

        View decorView = getWindow().getDecorView();
        //Activity's root View. Can also be root View of your layout (preferably)
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        //set background, if your root layout doesn't have one
        Drawable windowBackground = decorView.getBackground();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blurView.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new RenderScriptBlur(this))
                    .blurRadius(radius);
        }
        else {
            blurView.setupWith(rootView)
                    .windowBackground(windowBackground)
                    .blurAlgorithm(new SupportRenderScriptBlur(this))
                    .blurRadius(radius);
        }
    }

    private void gotoMainActivity() {
        logoContainer.animate().scaleX(0.5f).scaleY(0.5f).alpha(0).setDuration(350).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartupActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.anim_alpha_in, R.anim.nothing);
                finish();
                overridePendingTransition(R.anim.nothing, R.anim.anim_scale_out);
            }
        }, 400);
    }
}
