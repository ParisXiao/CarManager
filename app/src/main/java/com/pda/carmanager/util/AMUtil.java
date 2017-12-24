package com.pda.carmanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * 活动管理器
 * @param <T>
 */
public final class AMUtil<T extends Activity> {

    private ConcurrentHashMap<String , T>  map;



    private AMUtil() {
        map = new ConcurrentHashMap<>();
    }
    /**
     * 生成单例
     */
    static class Holder {
        static final AMUtil manager = new AMUtil();
    }

    /**
     * 返回实例
     * @return
     */
    public static AMUtil getManager() {
        return Holder.manager;
    }

    /**
     * 添加一个Activity到管理器
     * @param name
     * @param activity
     * @return
     */
    public T putActivity(String name, T activity) {
        return map.put(name, activity);
    }

    /**
     * 获取一个Activity
     * @param name
     * @return
     */
    public T getActivity(String name) {
        return map.get(name);
    }

    /**
     *  管理器是否为空
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     *  管理器中Activity的数量
     */
    public int size() {
        return map.size();
    }

    /**
     * 是否包含指定的Name
     * @param name
     * @return
     */
    public boolean containsName(String name) {
        return map.containsKey(name);
    }

    /**
     * 是否包含指定的Activity
     * @param activity
     * @return
     */
    public boolean containsActivity(T activity) {
        return map.containsValue(activity);
    }

    /**
     * 根据名称移除Activity
     * @param name
     */
    public void removeActivity(String name) {
        T activity = map.remove(name);
        Activity act = activity;
        if (act != null && !act.isFinishing()) {
            act.finish();
        }
    }

    /**
     * 根据Activity对象移除Activity
     * @param activity
     */
    public  void removeActivity(T activity) {
        Set<String> activityNames = map.keySet();
        for (String name : activityNames) {
            if (activity == map.get(name)) {
                removeActivity(name);
                return;
            }
        }
    }

    /**
     * 关闭所有活动的Activity除了指定的一个之外。
     * @param nameSpecified 指定的不关闭的Activity对象的名字。
     */
    public void removeAllActivityExceptOne(String nameSpecified) {
        Set<String> activityNames = map.keySet();
        T activitySpecified = map.get(nameSpecified);
        for (String name : activityNames) {
            if (!name.equals(nameSpecified)) {
                removeActivity(name);
            }
        }
        map.clear();
        map.put(nameSpecified, activitySpecified);
    }

    /**
     * 移除所有Activity
     */
    public void removeActivities() {
        Set<String> activityNames = map.keySet();
        for (String name : activityNames) {
            removeActivity(name);
        }
        map.clear();
    }

    /**
     * 启动Activity
     * @param context
     * @param clazz
     */
    public static void actionStart(Context context, Class clazz) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
        }
        context.startActivity(intent);
    }

    /**
     * 启动Activity
     * @param context
     * @param clazz
     * @param map
     */
    public static void actionStart(Context context, Class clazz, Map<String, Object> map) {
        Intent intent = new Intent(context, clazz);
        if (!(context instanceof Activity)) {
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof Integer) {
                //插入int数据
                intent.putExtra(entry.getKey(), (int) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                //插入字符串数据
                intent.putExtra(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Boolean) {
                //插入boolean数据
                intent.putExtra(entry.getKey(), (Boolean) entry.getValue());
            } else if (entry.getValue() instanceof Long) {
                //插入长整型数据
                intent.putExtra(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Float) {
                //插入浮点型数据
                intent.putExtra(entry.getKey(), (Float) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                //插入字符串数据
                intent.putExtra(entry.getKey(), (Double) entry.getValue());
            }else if (entry.getValue() instanceof Object) {
                intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
            }
        }
        context.startActivity(intent);
    }
    /**
     * Activity请求回调
     * @param activity
     * @param clazz
     */
    public static void actionStartForResult(Activity activity, Class clazz) {
        actionStartForResult(activity, clazz, 0);
    }

    /**
     * Activity请求回调
     * @param activity
     * @param clazz
     * @param requestCode
     */
    public static void actionStartForResult(Activity activity, Class clazz, int requestCode) {
        Intent intent = new Intent(activity, clazz);
        activity.startActivityForResult(intent, requestCode);
    }
}
