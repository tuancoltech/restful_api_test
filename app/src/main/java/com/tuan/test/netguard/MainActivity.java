package com.tuan.test.netguard;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvRequestResult;
    private ExecutorService mExecutorHttpRequest = Executors.newSingleThreadExecutor();
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_domain_1).setOnClickListener(this);
        findViewById(R.id.btn_domain_2).setOnClickListener(this);

        mTvRequestResult = findViewById(R.id.tv_request_result);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_domain_1:
                mTvRequestResult.setText("");
                doRequest("https://android.googleapis.com/gcm/notification");
                break;
            case R.id.btn_domain_2:
                mTvRequestResult.setText("");
                doRequest("https://android.googleapis.com/packages/ota-api/xiaomi_jasminesprout_jasmine/53e7f97e74ad88bd2d87d460cde46f1bca37e7cf.zip");
                break;

                default:
                    break;
        }
    }

    private void doRequest(final @NonNull String url) {
        mExecutorHttpRequest.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("tuancoltech", "Start executing request");
                    URL requestUrl = new URL(url);
                    HttpURLConnection httpConnection = (HttpURLConnection) requestUrl.openConnection();
                    httpConnection.setRequestMethod("GET");
                    httpConnection.setRequestProperty("Content-length", "0");
                    httpConnection.setUseCaches(false);
                    httpConnection.setConnectTimeout(100000);
                    httpConnection.setReadTimeout(100000);

                    httpConnection.connect();

                    int responseCode = httpConnection.getResponseCode();



                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Log.i("tuancoltech", "response code: HTTP_OK");
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("tuancoltech", "Request completes. Updating text view ...");
                                mTvRequestResult.setText("SUCCESSFUL at: " + System.currentTimeMillis());
                            }
                        });
                    } else {
                        Log.i("tuancoltech", "response code: " + responseCode);
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("tuancoltech", "Request completes. Updating text view ...");
                                mTvRequestResult.setText("ERROR at: " + System.currentTimeMillis());
                            }
                        });
                    }

                } catch (MalformedURLException e){
                    e.printStackTrace();
                    Log.e("tuancoltech", "Exception: " + e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("tuancoltech", "Exception: " + e.toString());
                }
            }
        });
    }
}
