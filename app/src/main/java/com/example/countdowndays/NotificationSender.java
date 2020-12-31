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
    private static NotificationChannel mChannel = null;
    private static NotificationManager notificationManager = null;
    private final static String ChannelID = "channel1";
    private final static String ChannelName = "noti1";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationSender(Context context){
        mChannel = new NotificationChannel(ChannelID, ChannelName, NotificationManager.IMPORTANCE_HIGH);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public static void  SendEventNotification(Event e, Context context){

        Intent intent = new Intent(context,SplashScreenActivity.class);
        PendingIntent pit = PendingIntent.getActivity(context, 0, intent, 0);
        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //NotificationChannel mChannel = new NotificationChannel("channel1", "noti1", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
            setsound(context,e,mChannel);
            notification = new Notification.Builder(context,ChannelID)
                    .setChannelId(ChannelID)//通道ID
                    .setContentTitle("您设定的纪念日快到啦")//消息标题
                    .setTicker(e.getTitle())//
                    .setContentIntent(pit)//点击消息跳转
                    .setAutoCancel(true)//点击消息自动取消
                    //.setWhen(System.currentTimeMillis())
                    .setWhen(e.getNotidate())
                    .setContentText(e.getNote())//消息内容
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();//消息大图标

            Log.d("1","channel");

        }
        notificationManager.notify(e.getId(), notification);
    }

    public static void CancelNotification(Event e){
        notificationManager.cancel(e.getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void setsound(Context context,Event e,NotificationChannel channel){
        String[] music_arr = context.getResources().getStringArray(R.array.music);
        String musicname = music_arr[e.getBgm()];
        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.lemon);
        Log.d("1",soundUri.toString());
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setSound(soundUri, audioAttributes);
        }
    }

}
