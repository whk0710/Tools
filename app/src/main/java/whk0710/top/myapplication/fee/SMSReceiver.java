package whk0710.top.myapplication.fee;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {

    private PhoneInfo phoneInfo;
    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();
            SmsMessage msg = null;
            if (null != bundle) {
                Object[] smsObj = (Object[]) bundle.get("pdus");
                for (Object object : smsObj) {
                    msg = SmsMessage.createFromPdu((byte[]) object);
                    Date date = new Date(msg.getTimestampMillis());//时间
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String receiveTime = format.format(date);
                    Toast.makeText(context,msg.getDisplayMessageBody(),Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
