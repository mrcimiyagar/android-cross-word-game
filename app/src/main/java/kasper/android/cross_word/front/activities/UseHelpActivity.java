package kasper.android.cross_word.front.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import kasper.android.cross_word.R;

public class UseHelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_help);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = getResources().getDisplayMetrics().widthPixels;

        getWindow().setAttributes(params);
    }

    public void onCancelBtnClicked(View view) {
        setResult(RESULT_OK, new Intent().putExtra("choice", "no"));
        this.finish();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onOkBtnClicked(View view) {
        setResult(RESULT_OK, new Intent().putExtra("choice", "yes"));
        this.finish();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }
}