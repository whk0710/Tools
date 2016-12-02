package whk0710.top.myapplication.fee;

import android.content.Context;
import android.provider.SyncStateContract;
import android.telephony.TelephonyManager;

import whk0710.top.myapplication.constants.Constants;

/**
 * Created by whk0710 on 2016/12/1.
 */
public class PhoneInfo {
    private TelephonyManager telephonyManager;
    /**
     * 国际移动用户识别码
     */
    private String IMSI;
    private Context context;
    public PhoneInfo(Context context) {
        this.context = context;
        telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 获取电话号码
     */
    public String getNativePhoneNumber() {
        String NativePhoneNumber=null;
        NativePhoneNumber=telephonyManager.getLine1Number();
        return NativePhoneNumber;
    }

    /**
     * 获取手机服务商信息
     */
    public String getProvidersName() {
        String ProvidersName = Constants.PROVIDER_NONE;
        try{
            IMSI = telephonyManager.getSubscriberId();
            // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
            System.out.println(IMSI);
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                ProvidersName = Constants.PROVIDER_CHINAMOBILE;
            } else if (IMSI.startsWith("46001")) {
                ProvidersName = Constants.PROVIDER_UNICOM;
            } else if (IMSI.startsWith("46003")) {
                ProvidersName = Constants.PROVIDER_CHINATELECOM;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ProvidersName;
    }
}
