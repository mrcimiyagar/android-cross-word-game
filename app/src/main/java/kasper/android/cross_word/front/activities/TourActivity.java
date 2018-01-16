package kasper.android.cross_word.front.activities;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders;
import kasper.android.cross_word.R;
import kasper.android.cross_word.back.callbacks.OnMyScoreUpdatedListener;
import kasper.android.cross_word.back.callbacks.OnMyTourDataReadListener;
import kasper.android.cross_word.back.callbacks.OnTourDataReadListener;
import kasper.android.cross_word.back.callbacks.OnTourPlayerAddedListener;
import kasper.android.cross_word.back.callbacks.OnTourPlayersReadListener;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.Me;
import kasper.android.cross_word.back.models.memory.TourPlayer;
import kasper.android.cross_word.back.models.memory.Tournament;
import kasper.android.cross_word.front.adapters.TourPlayerDataAdapter;

public class TourActivity extends AppCompatActivity {

    RelativeLayout contentContainer;
    TextView detailsTV;
    TableView tableView;
    CardView loadingView;
    LinearLayout myDataContainer;
    TextView myRankTV;
    TextView myNameTV;
    TextView myScoreTV;
    TextView registerContainer;

    boolean loading = false;

    int totalDays;

    int days, hours, minutes, seconds;

    Handler timerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        this.initViews();
        this.initTable();
        this.initContent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    private void initViews() {
        contentContainer = findViewById(R.id.activity_tour_content_container);
        detailsTV = findViewById(R.id.activity_tour_details_text_view);
        tableView = findViewById(R.id.activity_tour_table_view);
        loadingView = findViewById(R.id.activity_tour_loading_view);
        myDataContainer = findViewById(R.id.activity_tour_my_data_container);
        myRankTV = findViewById(R.id.activity_tour_my_rank_text_view);
        myNameTV = findViewById(R.id.activity_tour_my_name_text_view);
        myScoreTV = findViewById(R.id.activity_tour_my_score_text_view);
        registerContainer = findViewById(R.id.activity_tour_register_container);
    }

    private void initTable() {
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
    }

    private void initContent() {

        loading = true;

        loadingView.setVisibility(View.VISIBLE);
        contentContainer.setVisibility(View.GONE);

        registerContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!loading) {

                    loading = true;

                    loadingView.setVisibility(View.VISIBLE);

                    final Me me = MyApp.getInstance().getDatabaseHelper().getMe();

                    me.setScore(0);

                    MyApp.getInstance().getDatabaseHelper().updateMe(me);

                    MyApp.getInstance().getNetworkHelper().addTourPlayerToServer(me.getName(), me.getAccountNumber()
                            , new OnTourPlayerAddedListener() {
                        @Override
                        public void tourPlayerAdded(final long playerId, final String passkey) {

                            me.setPlayerId(playerId);
                            me.setPlayerKey(passkey);

                            MyApp.getInstance().getDatabaseHelper().updateMe(me);

                            if (me.getScore() + me.getMoney() > 0) {

                                MyApp.getInstance().getNetworkHelper().updateMyScoreInServer(playerId, passkey
                                        , me.getName(), me.getScore() + me.getMoney(), me.getAccountNumber()
                                        , new OnMyScoreUpdatedListener() {
                                            @Override
                                            public void myScoreUpdated() {

                                                MyApp.getInstance().getDatabaseHelper().updateMe(me);

                                                MyApp.getInstance().getNetworkHelper().readTourDataFromServer(new OnTourDataReadListener() {
                                                    @Override
                                                    public void tourDataRead(Tournament tournament) {

                                                        me.setCurrTour(tournament);
                                                        me.setLastTour(tournament);

                                                        MyApp.getInstance().getDatabaseHelper().updateMe(me);

                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                Toast.makeText(TourActivity.this, "ثبت نام انجام شد", Toast.LENGTH_SHORT).show();

                                                                initContent();
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                });
                            }
                            else {

                                MyApp.getInstance().getNetworkHelper().readTourDataFromServer(new OnTourDataReadListener() {
                                    @Override
                                    public void tourDataRead(Tournament tournament) {

                                        me.setCurrTour(tournament);
                                        me.setLastTour(tournament);

                                        MyApp.getInstance().getDatabaseHelper().updateMe(me);

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                Toast.makeText(TourActivity.this, "ثبت نام انجام شد", Toast.LENGTH_SHORT).show();

                                                initContent();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

        MyApp.getInstance().getNetworkHelper().readTourPlayersFromServer(new OnTourPlayersReadListener() {
            @Override
            public void tourPlayersRead(final List<TourPlayer> players) {

                Me me = MyApp.getInstance().getDatabaseHelper().getMe();

                if (me.getLastTour().getId() == me.getCurrTour().getId()) {

                    MyApp.getInstance().getNetworkHelper().readMyTourDataFromServer(me.getPlayerId(), me.getPlayerKey()
                            , new OnMyTourDataReadListener() {
                        @Override
                        public void myTourDataRead(final TourPlayer tourPlayer) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    myRankTV.setText(tourPlayer.getRank() + "");
                                    myNameTV.setText(tourPlayer.getName());
                                    myScoreTV.setText(tourPlayer.getScore() + "");

                                    contentContainer.setVisibility(View.VISIBLE);
                                    tableView.setDataAdapter(new TourPlayerDataAdapter(TourActivity.this, players));
                                    loadingView.setVisibility(View.GONE);
                                }
                            });

                            loading = false;
                        }
                    });
                }
                else {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            contentContainer.setVisibility(View.VISIBLE);
                            tableView.setDataAdapter(new TourPlayerDataAdapter(TourActivity.this, players));
                            loadingView.setVisibility(View.GONE);
                        }
                    });

                    loading = false;
                }
            }
        });

        Tournament lastTour = MyApp.getInstance().getDatabaseHelper().getMe().getLastTour();

        Tournament currTour = MyApp.getInstance().getDatabaseHelper().getMe().getCurrTour();

        if (currTour.getId() == lastTour.getId()) {

            registerContainer.setVisibility(View.GONE);

            if (currTour.isActive()) {
                myDataContainer.setVisibility(View.VISIBLE);
            }
            else {
                myDataContainer.setVisibility(View.GONE);
            }
        }
        else {

            myDataContainer.setVisibility(View.GONE);

            if (currTour.isActive()) {
                registerContainer.setVisibility(View.VISIBLE);
            }
            else {
                registerContainer.setVisibility(View.GONE);
            }
        }

        if (currTour.isActive()) {

            totalDays = currTour.getTotalDays();

            long leftMillis = currTour.getStartMillis() + (totalDays * 86400000) - System.currentTimeMillis();

            if (leftMillis >= 86400000) {
                days = (int)(leftMillis / 86400000);
                leftMillis = leftMillis % 86400000;
            }

            if (leftMillis >= 3600000) {
                hours = (int)(leftMillis / 3600000);
                leftMillis = leftMillis % 3600000;
            }

            if (leftMillis >= 60000) {
                minutes = (int)(leftMillis / 60000);
                leftMillis = leftMillis % 60000;
            }

            if (leftMillis >= 1000) {
                seconds = (int) (leftMillis / 1000);
                leftMillis = leftMillis % 1000;
            }

            timerHandler = new Handler();

            updateTimeUi();
        }
        else {
            detailsTV.setText("اسامی برندگان تورنمنت قبلی");
        }
    }

    private void updateTimeUi() {
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                seconds--;

                if (seconds < 0) {
                    seconds = 59;
                    minutes--;
                }

                if (minutes < 0) {
                    minutes = 59;
                    hours--;
                }

                if (hours < 0) {
                    hours = 23;
                    days--;
                }

                if (days < 0) {
                    initContent();
                }

                detailsTV.setText("تورنمنت " + totalDays + " روزه ," + "\n" + days + "," + hours + ":" + minutes + ":" + seconds + " باقی مانده است");

                updateTimeUi();
            }
        }, 1000);
    }
}
