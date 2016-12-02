package whk0710.top.myapplication.utils;

/**
 * Created by whk0710 on 2016/12/1.
 */
public final class Utils {
    /**
     * 创建logTag
     * @param cls
     * */
    public static String getLogTag(Class cls){
        return "This application -->" + cls.getSimpleName();
    }
}
