package com.example.countdowndays;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.countdowndays.model.Event;

public class NotificationSender {
    private static NotificationManager notificationManager = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationSender(Context context){
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void  SendEventNotification(Event e, Context context){

        NotificationChannel eventChannel = null;
        String Channel_id = "Channel "+e.getId();
        String Channel_name = "notification"+e.getId();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            eventChannel = new NotificationChannel(Channel_id,Channel_name,NotificationManager.IMPORTANCE_HIGH);
        }


        Intent intent = new Intent(context,SplashScreenActivity.class);
        PendingIntent pit = PendingIntent.getActivity(context, 0, intent, 0);
        Notification notification = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setsound(context,e,eventChannel);
            notificationManager.createNotificationChannel(eventChannel);
            notification = new Notification.Builder(context,Channel_id)
                    .setChannelId(Channel_id)//通道ID
                    .setContentTitle("您设定的纪念日快到啦")//消息标题
                    .setTicker(e.getTitle())//
                    .setContentIntent(pit)//点击消息跳转
                    .setAutoCancel(true)//点击消息自动取消
                    .setWhen(System.currentTimeMillis())
                    //.setWhen(e.getNotidate())
                    .setContentText(e.getNote())//消息内容
                    .setSmallIcon(R.mipmap.app_icon)
                    .build();

        }
        notificationManager.notify(e.getId(), notification);
    }

    public static void CancelNotification(Event e){
        notificationManager.cancel(e.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setsound(Context context,Event e,NotificationChannel notificationChannel){
        String[] music_arr = context.getResources().getStringArray(R.array.music);
        String musicname = music_arr[e.getBgm()];
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/raw/"+musicname);
        Log.d("1",soundUri.toString());
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("1","nnnnnnnn");
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationChannel.setSound(soundUri,audioAttributes);
        }
    }

}
