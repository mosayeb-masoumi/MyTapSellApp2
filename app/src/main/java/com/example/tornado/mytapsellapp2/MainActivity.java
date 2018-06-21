package com.example.tornado.mytapsellapp2;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAd;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.TapsellRewardListener;
import ir.tapsell.sdk.TapsellShowOptions;

import static ir.tapsell.sdk.TapsellAdRequestOptions.CACHE_TYPE_STREAMED;

public class MainActivity extends AppCompatActivity {

    Button btn_request,btn_show;
    TapsellAd ad;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
//        String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        Log.e("Tapsell", "android id:" + android_id);


        btn_request = findViewById(R.id.btn_request);
        btn_show = findViewById(R.id.btn_show_ad);


        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadAd("5b2bbe755219ec0001883623", TapsellAdRequestOptions.CACHE_TYPE_STREAMED);

            }
        });


        btn_show.setEnabled(false);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ad != null) {
                    btn_show.setEnabled(false);
                    TapsellShowOptions showOptions = new TapsellShowOptions();
                    showOptions.setBackDisabled(false);
                    showOptions.setImmersiveMode(true);
                    showOptions.setRotationMode(TapsellShowOptions.ROTATION_UNLOCKED);
                    showOptions.setShowDialog(true);
                    showOptions.setWarnBackPressedDialogMessage("سلام دوست من بک نزن");
                    showOptions.setWarnBackPressedDialogMessageTextColor(Color.RED);
                    showOptions.setWarnBackPressedDialogAssetTypefaceFileName("IranNastaliq.ttf");
                    showOptions.setWarnBackPressedDialogPositiveButtonText("ادامه بده");
                    showOptions.setWarnBackPressedDialogNegativeButtonText("ولم کن بزن بیرون");
                    showOptions.setWarnBackPressedDialogPositiveButtonBackgroundResId(R.drawable.button_background);
                    showOptions.setWarnBackPressedDialogNegativeButtonBackgroundResId(R.drawable.button_background);
                    showOptions.setWarnBackPressedDialogPositiveButtonTextColor(Color.RED);
                    showOptions.setWarnBackPressedDialogNegativeButtonTextColor(Color.GREEN);
                    showOptions.setWarnBackPressedDialogBackgroundResId(R.drawable.dialog_background);
//                    ad.show(MainActivity.this, showOptions);
                    ad.show(MainActivity.this, showOptions, new TapsellAdShowListener() {
                        @Override
                        public void onOpened(TapsellAd ad) {
                            Log.e("MainActivity", "on ad opened");
                        }

                        @Override
                        public void onClosed(TapsellAd ad) {
                            Log.e("MainActivity", "on ad closed");
                        }
                    });
                    MainActivity.this.ad = null;
                }
            }
        });



        Tapsell.setRewardListener(new TapsellRewardListener() {
            @Override
            public void onAdShowFinished(TapsellAd ad, boolean completed) {
                // store user reward if ad.isRewardedAd() and completed is true
                if(completed=true){
                    Toast.makeText(MainActivity.this, "you won!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "you lose!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }




    private void loadAd(final String zoneId, final int catchType) {

        if (ad == null) {
            TapsellAdRequestOptions options = new TapsellAdRequestOptions(catchType);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading ...");
            progressDialog.show();
            Tapsell.requestAd(MainActivity.this, zoneId, options, new TapsellAdRequestListener() {
                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, "ERROR:\n" + error, Toast.LENGTH_SHORT).show();
                    Log.e("Tapsell", error);
                    progressDialog.dismiss();
                }

                @Override
                public void onAdAvailable(TapsellAd ad) {

                    MainActivity.this.ad = ad;
                    btn_show.setEnabled(true);
                    Log.e("Tapsell", "adId: " + (ad == null ? "NULL" : ad.getId()) + " available, zoneId: " + (ad == null ? "NULL" : ad.getZoneId()));
                    progressDialog.dismiss();
//                new AlertDialog.Builder(MainActivity.this).setTitle("Title").setMessage("Message").show();
                }

                @Override
                public void onNoAdAvailable() {
                    Toast.makeText(MainActivity.this, "No Ad Available", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    Log.e("Tapsell", "No Ad Available");
                }

                @Override
                public void onNoNetwork() {
                    Toast.makeText(MainActivity.this, "No Network", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Log.e("Tapsell", "No Network Available");
                }

                @Override
                public void onExpiring(TapsellAd ad) {
                    btn_show.setEnabled(false);
                    MainActivity.this.ad = null;
                    loadAd(zoneId, catchType);
                }
            });
        }
    }


}
