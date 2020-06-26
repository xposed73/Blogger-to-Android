package com.blogger.lite.ui.activities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.blogger.lite.R;
import com.blogger.lite.data.models.Post;

public class PostDetail extends AppCompatActivity {

    Post post;
    WebView wv;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Bundle b = getIntent().getExtras();
        String json = b.getString("post");
        Gson gson = new Gson();
        post = gson.fromJson(json, Post.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionBarTitle(post.getTitle());

        wv = findViewById(R.id.wv);
        wv.setVerticalScrollBarEnabled(true);
        wv.setHorizontalScrollBarEnabled(true);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);

        String html = "<!DOCTYPE html><html><head>";
        html += "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />";
        html += "<style>img{max-width:100%;width: auto;height: auto;}iframe{max-width:100%;width: auto; height: auto;}</style>";
        html += "</head><body>"+ post.getContent()+"</body></html>";

        wv.loadData(html,"text/html; charset=utf-8", "utf-8");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );
                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
                if(taskList.get(0).numActivities == 1 &&
                        taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                    Intent i = new Intent(this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }else{
                    onBackPressed();
                }
                break;

        }

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            getDefaultShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    private void getDefaultShareIntent(){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, post.getTitle());
        i.putExtra(Intent.EXTRA_TEXT, post.getUrl());
    }

    private void setActionBarTitle(String title){
        SpannableStringBuilder ssb = new SpannableStringBuilder(title);
        getSupportActionBar().setTitle(ssb);
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            Class.forName("android.webkit.WebView")
                    .getMethod("onPause", (Class[]) null)
                    .invoke(wv, (Object[]) null);

            } catch(ClassNotFoundException cnfe) {
                Log.d("YT err", cnfe.getMessage());
            } catch(NoSuchMethodException nsme) {
                Log.d("YT err", nsme.getMessage());
            } catch(InvocationTargetException ite) {
                Log.d("YT err", ite.getMessage());
            } catch (IllegalAccessException iae) {
                Log.d("YT err", iae.getMessage());
            }

    }
}
