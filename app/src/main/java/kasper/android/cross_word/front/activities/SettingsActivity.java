package kasper.android.cross_word.front.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.Me;

public class SettingsActivity extends AppCompatActivity {

    EditText emailET;
    EditText nameET;

    private Me me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.emailET = findViewById(R.id.activity_account_email_edit_text);
        this.nameET = findViewById(R.id.activity_account_name_edit_text);

        this.me = MyApp.getInstance().getDatabaseHelper().getMe();

        emailET.setText(me.getEmail());
        nameET.setText(me.getName());
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    public void onOkBtnClicked(View view) {
        this.me.setEmail(emailET.getText().toString());
        this.me.setName(nameET.getText().toString());
        MyApp.getInstance().getDatabaseHelper().updateMe(this.me);
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }
}