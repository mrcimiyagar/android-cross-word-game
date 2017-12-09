package kasper.android.cross_word.front.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import kasper.android.cross_word.R;
import kasper.android.cross_word.back.callbacks.OnTourPlayersReadListener;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.TourPlayer;
import kasper.android.cross_word.front.adapters.TourPlayerDataAdapter;

public class TourActivity extends AppCompatActivity {

    RelativeLayout contentContainer;
    TableView tableView;
    CardView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        contentContainer = findViewById(R.id.activity_tour_content_container);
        tableView = findViewById(R.id.activity_tour_table_view);
        loadingView = findViewById(R.id.activity_tour_loading_view);

        loadingView.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);

        int colorEvenRows = Color.parseColor("#ffffffff");
        int colorOddRows = Color.parseColor("#ffeeeeee");
        tableView.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows));

        TableColumnWeightModel columnModel = new TableColumnWeightModel(3);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 5);
        columnModel.setColumnWeight(2, 2);
        tableView.setColumnModel(columnModel);

        tableView.setHeaderAdapter(new TableHeaderAdapter(this, 3) {
            @Override
            public View getHeaderView(int columnIndex, ViewGroup parentView) {

                TextView headerView = new TextView(getContext());
                headerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , LinearLayout.LayoutParams.MATCH_PARENT));
                headerView.setGravity(Gravity.CENTER);
                headerView.setTextColor(Color.WHITE);
                headerView.setTextSize(16);
                headerView.setPadding(0, (int) MyApp.getInstance().getDisplayHelper().dpToPx(16), 0,
                        (int) MyApp.getInstance().getDisplayHelper().dpToPx(16));

                switch (columnIndex) {
                    case 0:
                        headerView.setText("امتیاز");
                        break;
                    case 1:
                        headerView.setText("نام بازیکن");
                        break;
                    case 2:
                        headerView.setText("رتبه");
                        break;
                }

                return headerView;
            }
        });

        MyApp.getInstance().getNetworkHelper().readTourPlayersFromServer(new OnTourPlayersReadListener() {
            @Override
            public void tourPlayersRead(final List<TourPlayer> players) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        contentContainer.setVisibility(View.VISIBLE);
                        tableView.setDataAdapter(new TourPlayerDataAdapter(TourActivity.this, players));
                        loadingView.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }
}
