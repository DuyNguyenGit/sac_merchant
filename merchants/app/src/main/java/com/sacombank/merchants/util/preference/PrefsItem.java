package com.sacombank.merchants.util.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public abstract class PrefsItem {


    public static final String PREFS_NAME = "sacombank_preference";
    private String name;
    protected Context context;

    protected PrefsItem(Context context, String name) {
        this.name = name.intern();
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public void unset() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(getName()).apply();
    }

    public void clearAll() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear().apply();
    }

    protected abstract Object getPrefValue();

    protected abstract void setPrefValue(Object value);

    public static String convertObjectToString(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public static <T> T convertStringToObject(String string) {
        Gson gson = new Gson();
        return gson.fromJson(string, new TypeToken<T>(){}.getType());
    }

}
