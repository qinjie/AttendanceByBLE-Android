package edu.np.ece.attendancetakingapplication;

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by dellpc on 11/16/2017.
 */

public class ExitAppUtils {
    private List<Activity> mActivityList = new LinkedList<Activity>();
    private static ExitAppUtils instance = new ExitAppUtils();


    private ExitAppUtils(){};

    /**
     * 获取ExitAppUtils的实例，保证只有一个ExitAppUtils实例存在
     * @return
     */
    public static ExitAppUtils getInstance(){
        return instance;
    }


    /**
     * 添加Activity实例到mActivityList中，在onCreate()中调用
     * @param activity
     */
    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    /**
     * 从容器中删除多余的Activity实例，在onDestroy()中调用
     * @param activity
     */
    public void delActivity(Activity activity){
        mActivityList.remove(activity);
    }


    /**
     * 退出程序的方法
     */
    public void exit(){
        for(Activity activity : mActivityList){
            activity.finish();
        }

        System.exit(0);
    }
}
