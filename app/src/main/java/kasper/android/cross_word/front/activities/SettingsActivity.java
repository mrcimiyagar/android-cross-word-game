package kasper.android.cross_word.front.activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.callbacks.OnMyScoreUpdatedListener;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.Me;

public class SettingsActivity extends AppCompatActivity {

    TextInputEditText nameET;
    TextInputEditText accNumET;

    private Me me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.nameET = findViewById(R.id.activity_settings_name_edit_text);
        this.accNumET = findViewById(R.id.activity_settings_account_number_edit_text);

        this.me = MyApp.getInstance().getDatabaseHelper().getMe();

        nameET.setText(me.getName());
        accNumET.setText(me.getAccountNumber());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    public void onOkBtnClicked(View view) {

        if (nameET.getText() != null && nameET.getText().toString().length() > 0) {

            this.me.setName(nameET.getText().toString());
            this.me.setAccountNumber(accNumET.getText().toString());

            MyApp.getInstance().getDatabaseHelper().updateMe(this.me);

            if (me.getCurrTour().isActive() && me.getCurrTour().getId() == me.getLastTour().getId()) {

                MyApp.getInstance().getNetworkHelper().updateMyScoreInServer(me.getPlayerId()
                        , me.getPlayerKey(), me.getName(), me.getScore() + me.getMoney()
                        , me.getAccountNumber(), new OnMyScoreUpdatedListener() {
                            @Override
                            public void myScoreUpdated() {

                            }
                        });
            }

            onBackPressed();
        }
        else {
            Toast.makeText(this, "نام کاربری نمی تواند خالی باشد", Toast.LENGTH_SHORT).show();
        }
    }
}