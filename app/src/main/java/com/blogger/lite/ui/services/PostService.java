package com.blogger.lite.ui.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import com.blogger.lite.R;
import com.blogger.lite.data.AppDatabase;
import com.blogger.lite.data.dao.PostDao;
import com.blogger.lite.data.models.OcoPosts;
import com.blogger.lite.data.models.Post;
import com.blogger.lite.data.network.RestAdapter;
import com.blogger.lite.data.repository.PostRepository;
import com.blogger.lite.ui.activities.PostDetail;


public class PostService extends LifecycleService {
    // constant
    public static final long INTERVAL = 3600 * 1000; // 1 hour

    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
    private PostRepository postRepo;
    private PostDao dao;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        postRepo = new PostRepository(RestAdapter.createAPI());
        dao = AppDatabase.getDatabase(getApplicationContext()).postDao();

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.blogger.lite", MODE_PRIVATE);
        boolean notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled",true);
        if (notificationsEnabled) {
            if (mTimer != null)
                mTimer.cancel();
            else
                mTimer = new Timer(); // recreate new timer


            mTimer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    retrievePosts();
                }
            }, 0, INTERVAL);// schedule task
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent,flags,startId);
        return START_STICKY;
    }

    private void retrievePosts(){
        Observer<OcoPosts> observer = new Observer<OcoPosts>() {
            @Override
            public void onChanged(@Nullable OcoPosts res) {
                for (int i=0; i< res.getPosts().size(); i++){
                    Post post = res.getPosts().get(i);
                    if (dao.exists(post.getId()) == 0){
                        notifyUser(post);
                        dao.save(post);
                    }
                }
                postRepo.getPosts(null).removeObserver(this);
            }
        };

       //this.postRepo.getPosts(null).observe(this,observer);
    }

    private void notifyUser(final Post item){
        final Context ctx = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(ctx, PostDetail.class);
        Gson gson = new Gson();
        intent.putExtra("item", gson.toJson(item));

        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = new Notification.Builder(ctx)
                .setContentTitle(ctx.getResources().getString(R.string.app_name))
                .setContentText(item.getTitle())
                .setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .build();

        // hide the notification after its selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.contentIntent = pIntent;

        Integer nId = Integer.parseInt(item.getId().substring(0,4));

        notificationManager.notify(nId, notification);

    }

}