package com.lang.ktn.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.lang.ktn.base.BaseAppliton;

import java.util.Map;
import java.util.Set;

public class SharedPref {
    private Context mContext;
    private SharedPreferences mySharedPreferences;
    private static SharedPref sharedPref;

    public SharedPref(Context mContext) {
        this.mContext = mContext;
        //Constants.ANUO，save info named "info",mode:cant be read for other app
        mySharedPreferences = mContext.getSharedPreferences("GOGI", Context.MODE_PRIVATE);
    }

    public static SharedPref getInstance() {
        if (sharedPref == null) {
            synchronized (SharedPref.class) {
                if (sharedPref == null) {
                    sharedPref = new SharedPref(BaseAppliton.Companion.getApplication());
                }
            }
        }
        return sharedPref;
    }

    /**
     * save info ,different type , different save
     *
     * @param key
     * @param object
     */
    public void saveData(String key, Object object) {
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Set<?>) {
            editor.putStringSet(key, (Set<String>) object);
        } else if (object instanceof String) {
            editor.putString(key, (String) object);
        }
        editor.commit();
    }

    /**
     * get info from key-value
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        Map map = mySharedPreferences.getAll();
        if(map==null || !map.containsKey(key)){
            return null;
        }
        return map.get(key);
    }

}
