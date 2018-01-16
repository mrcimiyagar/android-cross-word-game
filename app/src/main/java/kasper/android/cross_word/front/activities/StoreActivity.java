package kasper.android.cross_word.front.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import kasper.android.cross_word.R;
import kasper.android.cross_word.back.callbacks.OnMyScoreUpdatedListener;
import kasper.android.cross_word.back.core.MyApp;
import kasper.android.cross_word.back.models.memory.Coins;
import kasper.android.cross_word.back.models.memory.Me;
import kasper.android.cross_word.util.IabHelper;
import kasper.android.cross_word.util.IabResult;
import kasper.android.cross_word.util.Inventory;
import kasper.android.cross_word.util.Purchase;

public class StoreActivity extends AppCompatActivity {

    RelativeLayout loadingView;

    static final String TAG = "KasperLogger";

    // SKUs for our products: the premium upgrade (non-consumable)
    static final String SKU_COIN = "53388653297";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static final int RC_REQUEST = 10001;

    // The helper object
    IabHelper mHelper;

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            try {
                Log.d(TAG, "Query inventory finished.");
                if (result.isFailure()) {
                    Log.d(TAG, "Failed to query inventory: " + result);

                    return;
                } else {
                    Log.d(TAG, "Query inventory was successful.");
                    // does the user have the premium upgrade?
                    mIsPremium = inventory.hasPurchase(SKU_COIN);

                    if (mIsPremium) {

                        mHelper.consumeAsync(inventory.getPurchase(SKU_COIN), mConsumeFinishedListener);
                    } else {

                    }

                    Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

                    loadingView.setVisibility(View.GONE);
                }

                Log.d(TAG, "Initial inventory query finished; enabling main UI.");

            } catch (Exception ignored) {

            }
        }
    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            try {
                if (result.isFailure()) {
                    Log.d(TAG, "Error purchasing: " + result);
                    return;
                } else if (purchase.getSku().equals(SKU_COIN)) {
                    mIsPremium = true;

                    Log.d(TAG, "purchase successful !");

                    mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                }
            } catch (Exception ignored) {

            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase, IabResult result) {
                    if (result.isSuccess()) {
                        // provision the in-app purchase to the user
                        // (for example, credit 50 gold coins to player's character)

                        Me me = MyApp.getInstance().getDatabaseHelper().getMe();
                        me.setMoney(me.getMoney() + coins.getStoreCoins());
                        MyApp.getInstance().getDatabaseHelper().updateMe(me);

                        MyApp.getInstance().getNetworkHelper().updateMyScoreInServer(me.getPlayerId()
                                , me.getPlayerKey(), me.getName(), me.getScore() + me.getMoney()
                                , me.getAccountNumber(), new OnMyScoreUpdatedListener() {
                                    @Override
                                    public void myScoreUpdated() {

                                    }
                                });

                        Toast.makeText(StoreActivity.this, "خرید با موفقیت انجام شد", Toast.LENGTH_SHORT).show();

                        loadingView.setVisibility(View.GONE);
                    }
                    else {
                        // handle error
                        Log.d(TAG, "error consuming product");
                    }
                }
            };

    int requestedIndex = 0;

    private TextView coinsCountTV;
    private Coins coins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        coinsCountTV = findViewById(R.id.activity_store_coins_count_text_view);

        loadingView = findViewById(R.id.activity_store_loading_view);
        loadingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        loadingView.setVisibility(View.VISIBLE);

        coins = MyApp.getInstance().getDatabaseHelper().getCoins();

        coinsCountTV.setText(coins.getStoreCoins() + " سکه");

        String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDW9BITfgFogkS5xVQNYRHFY7V+jV1PL4I3U5YJiqb0Bgk/6//As4Wcdyw6nHv7NmMYVG8l4uHgEn6zv12gdgG8qrjXtl1dVc+TNMRuRlmOkcaeCKkNNBAQWdCOdvoGj2BkZ3YqoMWA0kD5/MW/7FyeDBYS21gzb2pI7YjLmRVU17bYvEutGPobGv1+YpMcPNPH+D7OI28P3ECLt5L9tiA7RADd4RypsZ2h5Ne92VkCAwEAAQ==";
        // You can find it in your Bazaar console, in the Dealers section.
        // It is recommended to add more security than just pasting it in your source code;


        try {

            mHelper = new IabHelper(MyApp.getInstance(), base64EncodedPublicKey);

            Log.d(TAG, "Starting setup.");

            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {

                    try {

                        Log.d(TAG, "Setup finished.");

                        if (!result.isSuccess()) {
                            // Oh noes, there was a problem.
                            Log.d(TAG, "Problem setting up In-app Billing: " + result);
                        }

                        mHelper.queryInventoryAsync(mGotInventoryListener);

                    } catch (Exception ignored) {

                    }
                }
            });
        }
        catch (Exception ignored) {
            Toast.makeText(this, "لطفا اپ بازار را نصب کنید و به حساب خود وارد شوید", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {
            Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

            // Pass on the activity result to the helper for handling
            if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
                super.onActivityResult(requestCode, resultCode, data);
            } else {
                Log.d(TAG, "onActivityResult handled by IABUtil.");
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        try {
            if (mHelper != null) mHelper.dispose();
            mHelper = null;
        } catch (Exception ignored) {

        }
        super.onBackPressed();
        overridePendingTransition(R.anim.nothing, R.anim.anim_alpha_out);
    }

    public void onCloseBtnClicked(View view) {
        onBackPressed();
    }

    public void onBuyBtnClicked(View view) {
        mHelper.launchPurchaseFlow(this, SKU_COIN, RC_REQUEST, mPurchaseFinishedListener);
    }
}