package kasper.android.cross_word.front.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.Guide;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        TextView contentTV = findViewById(R.id.activity_guide_content_text_view);

        Guide guide = MyApp.getInstance().getDatabaseHelper().getGuide();

        contentTV.setText(guide.getContent());
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }
}
