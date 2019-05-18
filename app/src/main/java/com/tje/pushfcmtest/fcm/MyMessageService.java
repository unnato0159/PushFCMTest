package com.tje.pushfcmtest.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tje.pushfcmtest.MainActivity;

public class MyMessageService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d("새 토큰 발급",s);
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
         //super.onMessageReceived(remoteMessage);

        String pushTitle = remoteMessage.getNotification().getTitle();
        String pushBody = remoteMessage.getNotification().getBody();
        Log.d("푸시알림 제목",pushTitle);
        Log.d("푸시알림 내용",pushBody);

        showNotification(pushTitle,pushBody);
    }
    void showNotification(String title, String content){

        //알림을 누르면 어느화면으로 갈지?
        // MainActivity 이외의 화면으로도 이동 가능함
        Intent intent = new Intent(MyMessageService.this, MainActivity.class);

        // 화면에 액티비티가 쌓여 있으면 모두 제거하고 새 액티비티 호출,. 꼭해야하는거 아님
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);

        // 바로 띄우는게 아니라.  누르면 띄우도록 처리하기 위한 겉을 감싸는 인텐트 .
        // 이  PendingIntent 를 표시될 알림에 첨부

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        // 푸시알림이 왔을때 울릴 소리를 설정 연습 : v폰의 기본 알림소리 사용
        //  Uri :  어딘가로 향하는 경로를 지정할때 자주 사용
        Uri defaultNotiUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // 알림을 띄우는 역할을 담당하는 매니저를 안드로이드 시스템으로부터 얻어옴
        // 안드로이드 시스템 서비스는 여러가지의 종류를 내포 , 이번에 쓰는건 알림이라고 반드시 강제 캐스팅 .
        NotificationManager notificationManager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


    }
}
