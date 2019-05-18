package com.tje.pushfcmtest.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tje.pushfcmtest.MainActivity;
import com.tje.pushfcmtest.R;

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

        //알림 내용을 구성해주는 Builder  변수 생성
        NotificationCompat.Builder builder = null;

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
         // 안드로이드 버젼 코드가  0 보다 크다 ! 오레오 이상의 최신버전이다.
            // 알림 채널을 만들어서 해당 채널에 푸시알림을 던져주는 방식
// 알림 채널 생성 알림의 중요도를 보통으로 설정
            NotificationChannel channel = new NotificationChannel("app name","app name",NotificationManager.IMPORTANCE_DEFAULT);
            // 해당 알림 채널에 대한 설명뭉 = > 앱 설정에 들어가면 나타남
            channel.setDescription("푸시 알림 테스트용 채널입니다.");
            // 채널에서 조명 ( 카카오톡 조명 같은 기능) 을 사용할지 ?
            channel.enableLights(true);
            // 색깔은 뭘로 할건지 ?
            channel.setLightColor(Color.GREEN);
            // 진동 , 소리는 다시 한 번 열어줘야함 .
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1,1000});
            channel.setSound(defaultNotiUri,null);
            // 잠금화면에서 보여줄건지?
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            //채널을 실제로 만들어준다.
            notificationManager.createNotificationChannel(channel);
            // 이제 실제로 알림을 채널에 실어서 보낸다.
            builder = new NotificationCompat.Builder(this,channel.getId());
            //푸시알림 상태창에 뜨는 아이콘 설정
            builder.setSmallIcon(R.mipmap.ic_launcher);
            //알림의 제목 설정
            builder.setContentTitle(title);
            //알림의 내용 설정
            builder.setContentText(content);
            //알림의 울리는 설정
            builder.setSound(defaultNotiUri);
            // 알림이 왔을때 울리는 진동 패턴
            builder.setVibrate(new long[]{1,1000});
            // 자동삭제 되도록 할건지
            builder.setAutoCancel(true);
            // 이 알림을 누르면 어디로 갈지? 아까 만든 pending intent활용 지정
            builder.setContentIntent(pendingIntent);
            // 실제로 알림을 띄우는 부분

            // id 를 일반 숫자로 고정하면, 항상 같은 id 가 대입. => 여러번 알림이 오면 기존 알림을 덮어씀 .
            //만약 여러개의 알림을 모두 띄우고 싶다면 그때그때 다른 숫자가 들어가도록 코딩
            notificationManager.notify(1,builder.build());


        }
        else {
             //오레오 보다는 밑의 버전이다 . N 버전이하 누가 마쉬멜로우 롤리팝 아이스크림샌드위치
            // 알림 채널이라는 기능이 없어서 그냥 통일해서 쓰면 앱이 죽어버림.

            builder = new NotificationCompat.Builder(this);
            //푸시알림 상태창에 뜨는 아이콘 설정
            builder.setSmallIcon(R.mipmap.ic_launcher);
            //알림의 제목 설정
            builder.setContentTitle(title);
            //알림의 내용 설정
            builder.setContentText(content);
            //알림의 울리는 설정
            builder.setSound(defaultNotiUri);
            // 알림이 왔을때 울리는 진동 패턴
            builder.setVibrate(new long[]{1,1000});
            // 자동삭제 되도록 할건지
            builder.setAutoCancel(true);
            // 이 알림을 누르면 어디로 갈지? 아까 만든 pending intent활용 지정
            builder.setContentIntent(pendingIntent);
            // 실제로 알림을 띄우는 부분

            // id 를 일반 숫자로 고정하면, 항상 같은 id 가 대입. => 여러번 알림이 오면 기존 알림을 덮어씀 .
            //만약 여러개의 알림을 모두 띄우고 싶다면 그때그때 다른 숫자가 들어가도록 코딩
            notificationManager.notify(1,builder.build());



        }



    }
}
