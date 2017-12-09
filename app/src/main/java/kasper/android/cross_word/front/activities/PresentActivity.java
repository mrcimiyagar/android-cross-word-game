package kasper.android.cross_word.front.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import kasper.android.cross_word.R;

public class PresentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present);

        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        WindowManager.LayoutParams params = getWindow().getAttributes();

        params.width = getResources().getDisplayMetrics().widthPixels;
        params.height = getResources().getDisplayMetrics().heightPixels;

        getWindow().setAttributes(params);

        String title = getIntent().getExtras().getString("present-title");

        TextView titleTV = (TextView) findViewById(R.id.activity_present_title_text_view);
        titleTV.setText(title);

        String content = getIntent().getExtras().getString("present-content");

        TextView contentLayout = (TextView) findViewById(R.id.activity_present_content_layout);

        contentLayout.setText(content);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onOkBtnClicked(View view) {
        onBackPressed();
    }
}