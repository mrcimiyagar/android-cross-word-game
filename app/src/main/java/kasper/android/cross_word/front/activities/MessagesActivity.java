package kasper.android.cross_word.front.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.front.adapters.MessagesAdapter;
import kasper.android.cross_word.front.extras.LinearDecoration;

public class MessagesActivity extends AppCompatActivity {

    RelativeLayout listContainer;
    RecyclerView messagesRV;

    final int LIST_HIDE_ANIMATION = 400;
    final int LIST_SHOW_ANIMATION = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        this.listContainer = findViewById(R.id.activity_messages_list_container);

        this.messagesRV = findViewById(R.id.activity_messages_recycler_view);
        this.messagesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        this.messagesRV.addItemDecoration(new LinearDecoration((int) MyApp.getInstance().getDisplayHelper().dpToPx(16)
                ,(int) MyApp.getInstance().getDisplayHelper().dpToPx(16)));
        this.messagesRV.setAdapter(new MessagesAdapter(this, MyApp.getInstance().getDatabaseHelper().getMessages()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        listContainer.animate().alpha(1).setDuration(LIST_SHOW_ANIMATION).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    public void notifyOpeningWindow() {
        this.listContainer.animate().alpha(0).setDuration(LIST_HIDE_ANIMATION).start();
    }
}